package com.senko.pluginwithdatabase.entity;

/**
 * 持久层对象 玩家
 * @author senko
 * @date 2022/6/20 18:57
 */
public class PlayerPO {

    /**
     * 持久层PO对象，
     * 对于一些基本类型，建议改成包装类型，
     * 这样可以传入NULL值来避免出现默认值的情况。
     *
     * 后端人的必备常识（
     */
    private Integer id;     // 玩家ID
    private String name;    // 玩家名
    private String  uuid;  // 玩家等级
    private Integer money;  // 玩家金钱


    @Override
    public String toString() {
        return "PlayerPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", money=" + money +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public PlayerPO(Integer id, String name, String uuid, Integer money) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
        this.money = money;
    }
}
