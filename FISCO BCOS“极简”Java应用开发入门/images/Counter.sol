pragma solidity ^0.4.2;
contract Counter{
    string name;
    uint256 counter;
    event counted(uint256 c,uint256 oldvalue,uint256 currvalue,string memo);
    event saveoldvalue(uint256 oc);
    event changename(string oldname);
    function Counter(){
       name="I'm counter";
        counter = 101;
    }
    function getname()constant returns(string){
        return name;
    }

    function setname(string n){
    	name=n;
	changename(n);
    }



    function addcount(uint256 c,string memo)
    {
	uint256 oldvalue = counter;
	saveoldvalue(oldvalue); //event
        counter = counter+c;
        counted(c,oldvalue,counter,memo); //event
	
    }
    function getcount()constant returns(uint256){
        return counter;
    }
}
