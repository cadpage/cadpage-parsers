package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class CAButteCountyCParser extends DispatchH02Parser {
  
  public CAButteCountyCParser() {
    super(CITY_CODES,"BUTTE COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "CPDADMIN_NOREPLY@CHICOCA.GOV";
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
  
      "CHIC", "CHICO"

  }); 
}
