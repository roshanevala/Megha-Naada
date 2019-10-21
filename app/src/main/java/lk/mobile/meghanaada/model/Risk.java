package lk.mobile.meghanaada.model;

/**
 * Created by ravi on 16/11/17.
 */

public class Risk {
    String name;
    String district;
    String risk;
    String water_level;
    String water_level_near_river;
    String increased_water_level;
    String safest_location_near;

    public Risk(String name, String district, String risk, String water_level, String water_level_near_river, String increased_water_level, String safest_location_near) {
        this.name = name;
        this.district = district;
        this.risk = risk;
        this.water_level = water_level;
        this.water_level_near_river = water_level_near_river;
        this.increased_water_level = increased_water_level;
        this.safest_location_near = safest_location_near;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getWater_level() {
        return water_level;
    }

    public void setWater_level(String water_level) {
        this.water_level = water_level;
    }

    public String getWater_level_near_river() {
        return water_level_near_river;
    }

    public void setWater_level_near_river(String water_level_near_river) {
        this.water_level_near_river = water_level_near_river;
    }

    public String getIncreased_water_level() {
        return increased_water_level;
    }

    public void setIncreased_water_level(String increased_water_level) {
        this.increased_water_level = increased_water_level;
    }

    public String getSafest_location_near() {
        return safest_location_near;
    }

    public void setSafest_location_near(String safest_location_near) {
        this.safest_location_near = safest_location_near;
    }
}
