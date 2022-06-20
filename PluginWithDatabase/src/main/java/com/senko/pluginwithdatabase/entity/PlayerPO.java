package com.senko.pluginwithdatabase.entity;

/**
 * 持久层对象 玩家
 * @author senko
 * @date 2022/6/20 18:57
 */
public class PlayerPO {
    private Integer id;     // 玩家ID
    private String name;    // 玩家名
    private Integer level;  // 玩家等级
    private Integer money;  // 玩家金钱

    @Override
    public String toString() {
        return "PlayerPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public PlayerPO() {
    }

    public PlayerPO(Integer id, String name, Integer level, Integer money) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.money = money;
    }
}
