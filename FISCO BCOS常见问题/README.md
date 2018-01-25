# FISCO BCOS常见问题

## 部署相关问题

### 1.如何快速安装部署FISCO BCOS？

**解决方案**：作为范例，[一键快速安装部署](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/sample)提供了快速编译安装FISCO BCOS、并且部署2个节点的指南。

### 2.如何使用Docker安装FISCO BCOS？
**解决方案**：目前FISCO BCOS平台已经发布了Dockerfile文件和Docker Hub上预构建的镜像，并将持续同步源码的更新。想要使用Docker快速安装一个或者多个节点，请参阅[使用Docker安装部署BCOS指南。](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/docker)

### 3.在build路径运行"make -j2" 卡死
**解决方案**：编译的过程中需要从网络上下载依赖的包，网络条件太差可能卡死。建议在网络条件良好的环境搭建FISCO BCOS，或者从其他渠道下载依赖库包后拷贝到你的编译目标路径下。亦可参见[issue：make -j2 运行卡死](https://github.com/bcosorg/bcos/issues/20)

### 4.AWS亚马逊云安装问题

#### 4.1 AWS亚马逊云Centos-7.2安装FISCO-BCOS问题：

执行yum 显示No package cmake3 availale

```bash
[root@ip-172-31-1-126 fisco-bcos]# ./build.sh 
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
- base: mirrors.mit.edu
- extras: mirrors.umflint.edu
- updates: mirrors.tripadvisor.com
  No package cmake3 available.
  Error: Nothing to do
```

**解决方案**：

安装epel

```bash
sudo yum -y install epel-release
```

#### 4.2 AWS亚马逊云/阿里云 ubuntu16.04安装FISCO-BCOS问题：

执行apt显示 Unable to locate package lrzsz

```bash
root@ip-172-31-15-64:/fisco/fisco-bcos# apt install lrzsz
Reading package lists... Done
Building dependency tree       
Reading state information... Done
E: Unable to locate package lrzsz
```

**解决方案**：
执行 `apt-get update`



#### 4.3 部署合约报异常

```bash
root@ip-172-31-15-64:/fisco/fisco-bcos/tool# babel-node deploy.js HelloWorld
RPC=http://127.0.0.1:8545
Ouputpath=./output/
/fisco/fisco-bcos/tool/web3sync.js:65
let getBlockNumber = (() => {
^^^
SyntaxError: Block-scoped declarations (let, const, function, class) not yet supported outside strict mode
  at exports.runInThisContext (vm.js:53:16)
  at Module._compile (module.js:374:25)
  at loader (/usr/local/lib/node_modules/babel-cli/node_modules/_babel-register@6.26.0@babel-register/lib/node.js:144:5)
  at Object.require.extensions.(anonymous function) [as .js] (/usr/local/lib/node_modules/babel-cli/node_modules/_babel-register@6.26.0@babel-register/lib/node.js:154:7)
  at Module.load (module.js:344:32)
  at Function.Module._load (module.js:301:12)
  at Module.require (module.js:354:17)
  at require (internal/module.js:12:17)
  at Object.<anonymous> (/fisco/fisco-bcos/tool/deploy.js:12:16)
  at Module._compile (module.js:410:26)
```



**解决方案**：

查看nodejs版本

```
root@ip-172-31-15-64:/fisco/fisco-bcos/tool# node -v 
v4.2.6
```

Nodejs版本需要大于6：

```
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs
```

运行这个命令可以将nodejs升级到8以上的版本。



#### 4.4 npm ERR! enoent ENOENT: no such file or directory

**解决方案**：

查看nodejs是否已经安装，执行`node -v` 可查看
如果已经安装将build.sh一件安装脚本里面的

```bash
sudo npm install -g cnpm --registry=https://registry.npm.taobao.org
sudo cnpm install -g babel-cli babel-preset-es2017
echo '{ "presets": ["es2017"] }' > ~/.babelrc
```

屏蔽掉,重新执行。




## 智能合约相关问题


**1.如何运行智能合约？**

**解决方案**：目前FISCO BCOS平台已经发布了[contract_samples](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/contract_samples) (位于项目根目录下)，示范了使用Java 和 Node.js开发智能合约客户端的范例，展示了如何编译、部署、调用智能合约，供参考。更多问题细节，亦可参见[issue:智能合约问题](https://github.com/bcosorg/bcos/issues/35)





