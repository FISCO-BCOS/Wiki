package org.bcosliteclient;

import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.ECKeyPair;
import org.bcos.web3j.crypto.Keys;
import org.bcos.web3j.protocol.core.methods.response.EthBlockNumber;
import org.bcos.web3j.protocol.core.methods.response.EthGetWork;
import org.bcos.web3j.protocol.core.methods.response.Log;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcosliteclient.Counter.ChangenameEventResponse;
import org.bcosliteclient.Counter.CountedEventResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.bcos.channel.client.Service;
import org.bcos.channel.test.cns.CNSRpc;
import org.bcos.contract.source.ContractAbiMgr;
import org.bcos.contract.source.SystemProxy;
import org.bcos.contract.tools.CNSBaseOpr;
import org.bcos.contract.tools.CNSEle;
import org.bcos.contract.tools.SystemContractTools;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.channel.ChannelEthereumService;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CounterClient {
	static Logger logger = LoggerFactory.getLogger(bcosliteclient.class);
	public static Web3j web3j;
	// 初始化交易参数
	public static java.math.BigInteger gasPrice = new BigInteger("1");
	public static java.math.BigInteger gasLimit = new BigInteger("30000000");
	public static java.math.BigInteger initialWeiValue = new BigInteger("0");
	public static ECKeyPair keyPair;
	public static Credentials credentials;
    public static String contractAddress = "";
	/*deploy the contract,get address from blockchain*/
	public static void deployCounter() throws InterruptedException, ExecutionException
	{
		
		Future<Counter> futureDeploy = Counter.deploy(web3j, credentials, gasPrice, gasLimit, initialWeiValue);
		Counter counter = futureDeploy.get();
		contractAddress = counter.getContractAddress();
		counter.getContractName();
		logger.info("Deploy contract :"+counter.getContractName()+",address :"+contractAddress);
	}

	public static void testCounter() throws InterruptedException, ExecutionException {

		if(contractAddress.length()==0)
		{
			/*a test default value, change to the actually address first*/
			contractAddress = "0x4aceb2b9e22da391e50a3b1a2ae56b2bad430cea";
		}
		Counter counter = Counter.load(contractAddress, web3j, credentials, gasPrice, gasLimit);

		//get current counter value
		BigInteger val = counter.getcount().get().getValue();
		logger.info("counter value before transaction:"+val);
		Uint256 ival = new Uint256(100);
		Utf8String sval = new Utf8String("MyCounter from:" + val.intValue() + ",inc：" + ival.getValue());
        //send setname and add counter transaction at the same time  
		Future<TransactionReceipt> futureSetname = counter.setname(new Utf8String(sval.getValue()));
		Utf8String memo = new Utf8String("when tx done,counter inc "+ival.getValue().intValue());
		Future<TransactionReceipt> futureAddCount = counter.addcount(ival, memo);

		// waiting for new block
		TransactionReceipt receiptSetname = futureSetname.get();
		TransactionReceipt receiptAddAcount = futureAddCount.get();
        //get current name after transation commit
		String curName = counter.getname().get().getValue();

		/* process setname receipt */
		List<ChangenameEventResponse> lstCN = counter.getChangenameEvents(receiptSetname);
		for (int i = 0; i < lstCN.size(); i++) {
			ChangenameEventResponse response = lstCN.get(i);
			logger.info("setname-->oldname:[" 
			+ response.oldname.getValue()+"]," 
			+ "newname=[" + curName+"]");
		}


		//get current counter after transaction commit
		BigInteger curCounter = counter.getcount().get().getValue();
		logger.info("Current Counter:" + curCounter);
		
		
		/* process addcount transaction receipt */	
		List<Log> lstlog = receiptAddAcount.getLogs();
		List<CountedEventResponse> listresponse = counter.getCountedEvents(receiptAddAcount);
		for (int i = 0; i < listresponse.size(); i++) {
			CountedEventResponse response = listresponse.get(i);
			logger.info("addcount-->inc:" + response.c.getValue() 
					+ ",before：" + response.oldvalue.getValue()
					+ ",after：" + response.currvalue.getValue() 
					+ ",memo=" + response.memo.getValue());
		}
	}

	public static void main(String[] args) throws Exception {

		// init the Service
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Service service = context.getBean(Service.class);
		service.run(); // run the daemon service
		// init the client keys
		keyPair = Keys.createEcKeyPair();
		credentials = Credentials.create(keyPair);

		logger.info("-----> start test !");
		logger.info("init AOMP ChannelEthereumService");
		ChannelEthereumService channelEthereumService = new ChannelEthereumService();
		channelEthereumService.setChannelService(service);

		// init webj client base on channelEthereumService
		web3j = Web3j.build(channelEthereumService);
		/*------------------init done start test--------------------------------*/
		
		
		//test get blocknumber,just optional steps

		EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().sendAsync().get();
	    int startBlockNumber  =ethBlockNumber.getBlockNumber().intValue();
		logger.info("-->Got ethBlockNumber:{}", startBlockNumber);
 

		//deployCounter(); //if test deploy， uncomment this line
		testCounter();
		/*print block number after some transactions*/
		ethBlockNumber = web3j.ethBlockNumber().sendAsync().get();
		int finishBlockNumber  =ethBlockNumber.getBlockNumber().intValue();
		logger.info("<--startBlockNumber = "+startBlockNumber+",finish blocknmber="+finishBlockNumber);
		System.exit(0);
       
	}
}