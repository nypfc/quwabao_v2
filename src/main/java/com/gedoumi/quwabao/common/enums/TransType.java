package com.gedoumi.quwabao.common.enums;


public enum TransType {

    Init(0,"初始状态，无效"),
    Profit(1,"挖矿收益"),
    Reward(2,"推荐人收益"),
    RentBack(3,"租金退还"),
    TransOut(4,"交易转出"),
    TransIn(5,"交易转入"),
    Rent(6,"租用服务"),
    NetIn(7,"网关交易转入"),
    NetOut(8,"网关交易转出，提现"),
    FrozenIn(9,"天使钻转入"),
    FrozenOut(10,"天使钻转出"),
    UnFrozenAsset(11,"解冻资产"),
    TeamReward(12,"团队奖励"),
    TeamInit(13,"团队初始资产，天使币"),
    Bet(14,"下注"),
    GuessRight(15,"竞猜取胜")
    ;

    private TransType(int value, String name){
        this.value = value;
        this.name = name;
    }

    private int value;

    private String name;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static TransType fromValue(int value){
        TransType[] valus = TransType.values();
        for (TransType transType : valus) {
            if(transType.getValue() == value){
                return transType;
            }
        }
        return null;
    }
}
