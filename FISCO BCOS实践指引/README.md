
# FISCO BCOS实践指引  

**作者：fisco-dev**

<!-- TOC -->

## 目录
- [一、环境搭建](#一、环境搭建)  
    - [单节点环境](#11-单节点环境)  
    - [多节点组网](#12-多节点组网)  
    - [一键快速安装部署](#13-一键快速安装部署)  
    - [docker节点部署](#14-docker节点部署)  
    - [物料包工具](#15-使用物料包工具快速搭建环境)
- [二、特性简介](#二、特性简介)   
    - [权限控制](#21-权限控制)  
    - [CNS(合约命名服务)](#22-CNS(合约命名服务))  
    - [AMOP(链上信使协议)](#23-AMOP(链上信使协议))  
    - [共识机制](#24-应用于区块链的多节点并行拜占庭容错共识算法)  
    - [并行计算](#25-并行计算)  
- [三、业务实践](#三、业务实践)  
    - [web3sdk](#31-web3sdk)  
    - [存证sample](#32-存证sample)    
    - [区块链应用系统开发TIPS](#33-区块链应用系统开发TIPS)    

本文作为一个概括性的实践指引, 包含了FISCO BCOS的环境搭建、特性简介、业务实践的介绍预览, 可以使开发者对FISCO BCOS有一个全局性的认识, 更完整信息可以参考[Wiki](https://github.com/FISCO-BCOS/Wiki)。
## 一、环境搭建  
本段落给出了FISCO BCOS多种环境搭建方式, 从快速体验到逐步搭建, 满足各类开发者的需求。
- ### 单节点环境  
  指引开发者编译、搭建一个最简单的由一个节点组成的环境, 并进行合约的部署、调用; 同时，能够进行系统合约的部署, 对系统合约有一个简单的认识, 为搭建多节点链做准备。  
1. [源码编译](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/doc/manual#13-编译源码) ： 搭建FISCO BCOS的配置要求, 软件依赖安装, 源码获取, 源码编译介绍。 
2. [创建创世节点(单节点环境搭建)](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/doc/manual#第二章-创建创世节点) ：搭建只有一个节点的区块链环境, 创世节点是区块链中的第一个节点, 搭建多节点的链环境, 也需要从创世节点开始。 
3. [部署调用合约](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/doc/manual#第三章-部署合约调用合约)  ：正确部署、运行sample合约。
4. [部署系统合约](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/doc/manual#第四章-部署系统合约) : FISCO BCOS区块链为了满足准入控制、身份认证、配置管理、权限管理等需求，在网络启动之初，会部署一套功能强大、结构灵活且支持自定义扩展的智能合约，统称系统合约, 详细了解系统合约可以参考：[系统合约介绍](https://github.com/FISCO-BCOS/Wiki/tree/master/FISCO-BCOS%E7%B3%BB%E7%BB%9F%E5%90%88%E7%BA%A6%E4%BB%8B%E7%BB%8D)。
- ### 多节点组网  
1. [多节点组网](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/doc/manual#第六章-多节点组网) : 搭建多个节点的链环境, 包括节点的加入、退出功能。
2. [机构证书准入](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/doc/manual#第七章-机构证书准入) : FISCO BCOS提供了证书准入的功能。在节点加入网络后，节点间是否能够通信，还可通过证书进行控制。在FISCO BCOS中，节点的证书代表了此节点属于某个机构。FISCO BCOS区块链中的管理者，可以通过配置机构的证书，控制相应证书的节点是否能够与其它节点通信。
- ### 一键快速安装部署
  [一键快速安装部署](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/sample)：为了能够让初学者快速体验, FISCO BCOS平台提供了快速安装和节点的快速部署工具, 开发者只需要非常简单的命令既可成功搭建多个节点的环境, 不再需要繁琐的配置。  
- ### Docker节点部署  
  [Docker节点部署](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master/docker)：FISCO BCOS同样提供了docker下的节点安装流程, 使开发者可以快速在docker环境下搭建、运行、体验。  
- ### 物料包工具  
  [物料包工具](https://github.com/FISCO-BCOS/fisco-package-build-tool): 使用本工具, 进行一些简单配置后, 可以创建区块链节点的安装包, 然后经过一些简单的步骤, 可以快速搭建生产可用的区块链环境。 

## 二、特性简介
- ### 权限控制  
  [权限模型](https://github.com/FISCO-BCOS/Wiki/tree/master/FISCO%20BCOS%E6%9D%83%E9%99%90%E6%A8%A1%E5%9E%8B)：与可自由加入退出、自由交易、自由检索的公有链相比，联盟链有准入许可、交易多样化、基于商业上隐私及安全考虑、高稳定性等要求。因此，联盟链在实践过程中需强调“权限”及“控制”的理念。
  为体现“权限”及“控制”理念，FISCO BCOS平台基于系统级权限控制和接口级权限控制的思想，提出ARPI(Account—Role—Permission—Interface)权限控制模型。
- ### CNS(合约命名服务)  
  [CNS(Contract Name Service)](https://github.com/FISCO-BCOS/Wiki/tree/master/Contract_Name%20Service%E6%9C%8D%E5%8A%A1)：提供一种命令服务, 将合约接口调用映射为名称, 并且内置了合约的版本管理, 基于CNS开发者可以以更简单的方式调用合约接口, 可以忽略更多的繁琐流程, 而且基于CNS内置的合约版本管理机制对于合约的灰度升级会非常友好。
- ### AMOP(链上信使协议)
  [AMOP(Advance Messages Onchain Protocol)](https://github.com/FISCO-BCOS/Wiki/tree/master/AMOP%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97)：AMOP系统旨在为联盟链提供一个安全高效的消息信道，联盟链中的各个机构，只要部署了区块链节点，无论是共识节点还是观察节点，均可使用AMOP进行通讯。  
- ### 应用于区块链的多节点并行拜占庭容错共识算法  
  [共识机制](https://github.com/FISCO-BCOS/Wiki/tree/master/%E5%BA%94%E7%94%A8%E4%BA%8E%E5%8C%BA%E5%9D%97%E9%93%BE%E7%9A%84%E5%A4%9A%E8%8A%82%E7%82%B9%E5%B9%B6%E8%A1%8C%E6%8B%9C%E5%8D%A0%E5%BA%AD%E5%AE%B9%E9%94%99%E5%85%B1%E8%AF%86%E7%AE%97%E6%B3%95)：FISCO BCOS的共识机制是在PFBT基础上进行改进, 结合区块链的特性, 增加了异常处理, 添加了并行处理的机制。
- ### 并行计算  
  [并行计算](https://github.com/FISCO-BCOS/Wiki/tree/master/FISCO-BCOS%E5%B9%B6%E8%A1%8C%E8%AE%A1%E7%AE%97%E4%BB%8B%E7%BB%8D)：区块链的系统特性决定，在区块链中增加节点，只会增强系统的容错性，增加参与者的授信背书等，而不会增加性能，只增加节点不能解决问题，这就需要通过架构上的调整来应对性能挑战，所以，我们提出了“并行计算，多链运行”的方案。并行多链的架构基本思路是在一个区块链网络里，存在多个分组，每个组是一个完整的区块链网络，有独立的软件模块，硬件资源，独立完成机构间共识，有独立的数据存储。
> 完整的特性介绍可以参考：
> [FISCO BCOS特性介绍](https://github.com/FISCO-BCOS/Wiki/blob/master/FISCO%20BCOS%E7%89%B9%E6%80%A7%E4%BB%8B%E7%BB%8D.pdf)
## 三、业务实践  
本模块介绍FISCO BCOS客户端web3sk的使用, 并在此基础上给出了一个工业级的生产案例--存证sample。
### web3sdk  
  web3sdk是FISCO BCOS的java客户端, 针对FISCO BCOS做了多项优化、改进, 添加了FISCO BCOS的多项特性。    
  [下载地址](https://github.com/FISCO-BCOS/web3sdk)    
  [使用文档](https://github.com/FISCO-BCOS/web3sdk/blob/master/README.md)    
###  存证sample
  [源码地址](https://github.com/FISCO-BCOS/Wiki/tree/master/%E5%AD%98%E8%AF%81sample%E8%AF%B4%E6%98%8E)  
  FISCO BCOS是聚焦于金融级应用服务的区块链底层技术平台。在此基础上，FISCO BCOS团队结合区块链不可篡改、多方共识等特性，开发此sample以帮助开发者快速入门区块链存证应用开发。  
  本sample建立了完整的存证、核证、取证业务模型，并允许司法机构参与到业务过程中实时见证。为后续的证据核实、纠纷解决、裁决送达提供了可信、可追溯、可证明的技术保障。  
### 区块链应用系统开发TIPS  
   [文档地址](https://github.com/FISCO-BCOS/Wiki/tree/master/%E5%8C%BA%E5%9D%97%E9%93%BE%E5%BA%94%E7%94%A8%E7%B3%BB%E7%BB%9F%E5%BC%80%E5%8F%91TIPS)
