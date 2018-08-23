# 存证sample说明
**作者：fisco-dev**  

## 1. 概要
FISCO BCOS是聚焦于金融级应用服务的区块链底层技术平台。在此基础上，FISCO BCOS团队结合区块链不可篡改、多方共识等特性，开发此sample以帮助开发者快速入门区块链存证应用开发。  
本sample建立了完整的存证、核证、取证业务模型，并允许司法机构参与到业务过程中实时见证。为后续的证据核实、纠纷解决、裁决送达提供了可信、可追溯、可证明的技术保障。  
接下来，我们将从特性、适用场景及使用说明三个方面，对sample进行介绍。

## 2. 特性
本sample是基于FISCO BCOS开发的应用案例，为解决传统存证业务痛点，构建了以下核心特性：  

1. 多方共识，支持监管接入：每个节点均拥有达成共识的数据，去中心化。司法和监管机构可作为监管节点接入。
2. 防纂改、可追溯：利用区块链本身特性，永久记录事件的发生，并可实时追溯举证。
3. 隐私保护：使用数字加密和零知识证明技术，只有关键信息经HASH运算后上链。确保数据完整的同时，保护各方隐私。
4. 扩展性强：使用两类智能合约对存证进行管理，使系统获得更好的扩展性。 
- 工厂合约（EvidenceSignersData.sol）：由存证各方事前约定，存储存证生效条件，并管理存证的生成。
- 存证合约（Evidence.sol）：由工厂合约生成，存储存证id、hash和各方签名（每张存证一个合约）。

## 3. 适用场景
本sample适用于所有需要进行存证、核证、取证的业务场景，尤其是需要解决多方信任问题或获取司法监管许可的情况。以下列举了部分可以使用该sample快速支持的业务场景：
* 交易记录：记录某类交易的发生，完整记录交易双方信息和时间。如买、卖、经纪多方共同确认一笔股权交易并进行存证，第三方监管机构可基于链上存证进行追溯和审计。
* 电子合同管理：可将合同签署的全流程存证上链，每个里程碑的存证均由参与节点签名确认，完整记录当时的状态和版本，并支持司法、监管机构实时查询、举证。
* 供应链管理：可将供应链环境中各方合同存证上链，从而实现违约后举证和维权。同时也可以在链上记录关键数据，对供应链健康度进行管控和分析。

## 4. 存证案例运行环境搭建
1. 本文档使用单个区块链节点来模拟区块链环境。
2. 搭建FISCO BCOS区块链环境（参考FISCO BCOS使用文档），操作系统为Ubuntu(建议16.04)或CentOS（建议7.2）。
3. 将存证客户端导入Eclipse（本说明文档以Eclipse为例），配置JDK（1.8）。  
   存证客户端下载URL：https://github.com/FISCO-BCOS/evidenceSample/tree/master/evidence
4. 在配置文件applicationContext.xml中配置区块链节点信息，具体参照5.2节区块链节点信息配置。
5. 更新签名机构公私钥（示例演示可以直接使用sample提供公私钥），公钥在applicationContext.xml文件中配置，私钥需替换/evidence/src/main/resources下的私钥文件，具体参照5.1节中角色公钥配置说明。
 
## 5.存证案例配置文件说明

```
evidence/src/main/resources/applicationContext.xml文件配置说明
```

### 5.1 角色公钥配置说明

	<bean id="addressConf" class="org.bcos.evidence.sample.PublicAddressConf">
		<property name="allPublicAddress">
			<map>
				<entry key="User" value="0x33674063c4618f4773fac75dc2f07e55f6f391ce">
				</entry>
				<entry key="Arbitrator" value="0x6bc952a2e4db9c0c86a368d83e9df0c6ab481102">
				</entry>
				<entry key="Depositor" value="0x5a6c7ccf9efa702f4e8888ff7e8a3310abcf8c51">
				</entry>
			</map>
		</property>
	</bean>

配置解释：

* 配置中key=User代表用户角色，对应的私钥文件为user.jks；key=Arbitrator代表仲裁机构，对应的私钥文件为arbitrator.jks；key=Depositor代表存证机构，对应的私钥文件为depositor.jks。

* 按照上面的key-value的格式写入3个角色所对应的公钥，在/evidence/src/main/resources这个文件夹下放入角色所对应的私钥。
私钥是由linux系统下java JDK/bin中的keytool工具生成的（生成命令如下），私钥生成后，可以通过私钥调用接口生成需要的公钥，具体操作可参照<a name="host_node" id="host_node">6.6. 获取公钥</a>

        keytool -genkeypair -alias ec -keyalg EC -keysize 256 -sigalg SHA256withECDSA  -validity 365 -storetype JKS -
        keystore user.jks -storepass 123456


### 5.2 区块链节点信息配置

     <bean id="channelService" class="cn.webank.channel.client.Service">
		<property name="orgID" value="User" /><!-- 配置角色名称 -->
		<property name="allChannelConnections">
			<map>
 		       <entry key="User"><!-- 配置本角色的区块链节点列表-->
 					<bean class="cn.webank.channel.handler.ChannelConnections">
 						<property name="connectionsStr">
 							<list> 
                               <value>User@127.0.0.1:8541</value><!-- 格式：节点nodeId@IP地址:链上链下端口-->
 							</list>
 						</property>
 					</bean>
				</entry>
			</map>
		</property>
	</bean>



## 6. 存证案例工具包使用说明

本节提供使用示例工具包，以便开发者能够快速熟悉存证应用。在工具包中，bin文件下为执行脚本，conf文件夹下为工具包配置文件，lib文件下为存证案例依赖包,contracts中存放合约源码（合约java代码生成可以参照6.7）

* 存证工具包可以通过存证客户端gradle run生成；或者直接下载，下载URL：https://github.com/FISCO-BCOS/evidenceSample/tree/master/evidence_toolkit
* 下载完成之后建议对bin文件夹下的文件执行chmod命令。
* 安装solc solidity编译器（直接使用远程二机制源或源码安装）
	Ubuntu安装 apt install solc 或 源码安装
	CentOS安装 yum install solc 或 源码安装
* 若想查看完整的执行过程，可执行存证工具包bin文件下runEvidence.sh脚本。
* 根据需求情况更新公私钥（需要3组），公钥以key-value的形式在applicationContext.xml中配置，私钥更新需要替换conf文件下的私钥文件。若无特殊需求可以不用更新公私钥。
* 在applicationContext.xml文件中配置区块链节点信息，具体参照上文。

### 6.1 工厂合约部署

进入到bin文件下，输入以下命令：

```
./evidence deploy keyStoreFileName keyStorePassword keyPassword
例子：./evidence deploy user.jks '123456' '123456'
```

参数说明：

* 在上面的命令中需要传入4个参数，第一个参数固定不可修改。后面三个参数依赖conf中的私钥的文件(直接输入文件名，并非路径)。keyStoreFileName：私钥文件名、keyStorePassword：keyStore密码、key：密码。

控制台显示结果：

    deploy factoryContract success, address: deployAddress.（部署工厂合约返回地址）


### 6.2 用户创建新证据

进入到bin文件下，在控制台输入命令：

```
./evidence new  keyStoreFileName keyStorePassword keyPassword deployAddress evidence_id evidence_hash
例子：./evidence new user.jks '123456' '123456' '0x19e2f046f4fc6a02d732a3ffda6480c34214f57f' '1' '0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee'
```

参数说明：

* 命令中第一个参数不可修改，deployAddress为工厂合约部署之后返回的地址，evidence_id和evidence_hash分别为证据ID和证据的hash值，长度无限制。证据的hash可以是合同的hash值，还款记录的hash等。计算方法可以是md5值，sha1，sha3等。

控制台显示结果：

    newEvidence success, newEvidenceAddress: newEvidenceAddress（创建证据返回地址）
	

### 6.3 发送签名上链

进入到bin文件下，在控制台输入命令：


     ./evidence send keyStoreFileName keyStorePassword keyPassword newEvidenceAddress

     例子：./evidence send arbitrator.jks '123456' '123456' '0x4437863afe7c9adce4e658c95666feaab1d996a2' 
          ./evidence send depositor.jks '123456' '123456' '0x4437863afe7c9adce4e658c95666feaab1d996a2' 


参数说明：

* 注意：此命令需要执行两次，在newEvidence步骤时，用户已经签名，在这个步骤需要存证机构和仲裁机构双方签名，所以在执行这个命令时传入的keyStoreFileName、keyStorePassword、keyPassword分别为两个机构的私钥及密码。
* 在这个步骤中同样需要用到evidence_hash值，程序中通过newEvidenceAddress查询证据的信息，然后获取evidence_hash值。
* 命令中第一个参数不可修改，newEvidenceAddress为创建证据区块链返回的地址。

控制台显示结果：

    sendSignatureToBlockChain success：true


### 6.4 获取证据

进入到bin文件下，在控制台输入命令：

```
./evidence get keyStoreFileName keyStorePassword keyPassword newEvidenceAddress
例子：./evidence get user.jks '123456' '123456' '0x4437863afe7c9adce4e658c95666feaab1d996a2'
```

参数说明：

* 命令中第一个参数不可修改，在这个命令中依赖conf中的私钥文件和deploy操作相同，newEvidenceAddress为创建证据后区块链返回的地址。

控制台显示结果：

    the evidenceID:evidence_id(创建证据时传入的evidence_id值)
    the evidenceHash：evidence_hash( 创建证据时传入的evidence_hash值)
    the signature[0-2]：（打印3个角色的签名）
    the publicKey[0-2]：（打印3个角色的公钥）

### 6.5 校验证据和签名

进入到bin文件下，在控制台输入命令：

```
./evidence verify keyStoreFileName keyStorePassword keyPassword newEvidenceAddress
例子：./evidence verify user.jks '123456' '123456' '0x4437863afe7c9adce4e658c95666feaab1d996a2'
```

参数说明：

* 命令中第一个参数不可修改，在这个命令中依赖conf中的私钥文件和deploy操作相同，newEvidenceAddress为创建证据后区块链返回的地址。

控制台结果显示：

    若校验通过显示：verifyEvidence success：true
	若校验失败显示：verifyEvidence failed:false


### 6.6 获取公钥
进入到bin文件下，在控制台输入命令：

```
./evidence getPublicKey keyStoreFileName keyStorePassword keyPassword 
例子：./evidence getPublicKey user.jks '123456' '123456'
```

参数说明：

* 命令中第一个参数不可修改，通过读取对应的私钥文件，获取公钥。

控制台结果显示：

    publicKey：公钥

### 6.7 合约编译及java Wrap代码生成
 
* 下载存证案例工具包
* 智能合约语法及细节参考 <a href="https://solidity.readthedocs.io/en/develop/solidity-in-depth.html">solidity官方文档</a>，需要下载solidity编译器版本，指定为0.4.2及以上，合约编写可以使用任何文本编辑器（推荐使用sublime或vs code+solidity插件）
* contracts文件夹中有存证案例的合约源码，bin文件夹为编译执行目录，lib为依赖库，output（不需要新建，脚本会生成）为编译后输出的abi、bin及java文件目录
* 若想编译自己的智能合约，需要将自己的合约复制到contracts文件夹下。（编译前需要在此文件夹下需要先删掉其他无关合约）
* 在bin文件夹下compile.sh为编译合约的脚本，执行命令sh compile.sh [参数1：java包名]执行成功后将在output目录生成所有合约对应的abi,bin,java文件，其文件名类似：合约名字.[abi|bin|java]。compile.sh脚本执行步骤实际分为两步，1.首先将sol源文件编译成abi和bin文件，依赖solc工具；2.将bin和abi文件编译java Wrap代码，依赖web3sdk

## 7. 存证客户端使用

存证客户端的入口为org.bcos.evidence.app.Main类，客户端中对合约的调用主要包括：web3j的初始化，合约对象部署，载入已经部署的合约，创建证据，发送签名数据，获取证据信息，以及证据校验。区块链应用程序实际是通过web3j生成的java Wrapper类(详细介绍参看6.7合约编译及java Wrap代码生成)，通过jsonRPC调用和FISCO BCOS客户端节点通信，再由客户端返回jsonRPC请求响应。

### 7.1 web3j初始化 

注意：客户端调用智能合约首先必须初始化web3j，初始化关键代码如下：


        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        web3j = Web3j.build(channelEthereumService);


### 7.2 工厂合约部署

使用初始化的web3j对像来部署智能合约，如果工厂合约部署成功，Future对象会返回合约调用对象（即合约地址），合约部署关键代码如下：


```
EvidenceSignersData evidenceSignersData = EvidenceSignersData.deploy(web3j, credentials, gasPrice, gasLimited, initialValue,evidenceSigners).get();
```

### 7.3 载入已经部署的合约


合约部署成功后，可以获取到已经部署的合约地址：evidenceSignersData.getContractAddress()；使用已经部署合约地址，初始化的web3j对象，可以载入智能合约调用对象。

```
EvidenceSignersData evidenceSignersData = EvidenceSignersData.load(address.toString(), web3j,  credentials, gasPrice, gasLimited);
```

注意：部署后可以直接使用返回的智能合约对象，而无需再load载入！

### 7.4 创建新证据


发送交易通过直接调用已经部署或载入的智能合约调用对象执行合约对应接口即可，交易执行成功后将返回Receipt，Receipt包含交易hash和其他信息。

```
Receipt receipt = evidenceSignersData.newEvidence(new Utf8String(evidence_hash),new Utf8String(evidence_id),new Utf8String(evidence_id),new Uint8(signatureData.getV()),new Bytes32(signatureData.getR()),new Bytes32(signatureData.getS())).get();
```

注意：调用此方法需要传入参数，参数为string[]，长度为3即可。传入的三个参数分别模拟对应evidence_id(证据ID)、evidence_hash(证据hash值)、sign_data（签名数据），客户端将传入的三个参数生成一个完成的证据存入区块链中。

### 7.5 发送签名数据上链

在用户角色调用newEvidence（）接口创建一个新的证据之后，存证机构和签名机构分别需要对证据进行签名，并且将签名的数据发送到区块链中进行存储，关键代码如下：

```
Evidence evidence = Evidence.load(address, web3j, credentials,  gasPrice, gasLimited);
TransactionReceipt receipt = evidence.addSignatures(new Uint8(signature.getV()),
new Bytes32(signature.getR()),
new Bytes32(signature.getS())).get();
```

### 7.6 获取证据

创建证据一个，区块链会返回一个交易地址，使用已经部署或载入的智能合约对象调用getEvidence()接口传入交易返回的地址，可返回一个list<Type>,对list<Type>进行解析可以得到一个evidence对象。

```
Evidence evidence = Evidence.load(newEvidenceAddres, web3j, credentials,  gasPrice, gasLimited);
List<Type> result2 = evidence.getEvidence().get();
```

### 7.7 校验证据和签名

通过调用步骤7.6中的接口可以获取到证据的完整信息，并且附带有3个角色对证据的签名信息，通过校验公钥是否相同来确定签名的信息是否准确。关键代码如下：



    for (String str : data.getSignatures()) {
        try {
            addressList.add(verifySignedMessage(data.getEvidenceHash(), str));
        } catch (SignatureException e) {
            throw e;
        }
    }
    for (String addr : data.getPublicKeys()) {
        boolean flag = false;
        for (String str : addressList) {
            if (str.equals(addr)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return false;
        }
    }





如果您觉得本文不错，欢迎[戳这里](https://github.com/FISCO-BCOS/FISCO-BCOS)给FISCO BCOS打star:star:。
