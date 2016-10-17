package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

public class ALJacksonCountyParser extends DispatchA37Parser {

  public ALJacksonCountyParser() {
    super(null,CITY_LIST, "JACKSON COUNTY", "AL");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public String getFilter() { 
    return "ALDispatch@scottsboro.org"; 
  }

  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*) (AL|GA|TN)(?: \\d{5})?");
  
  @Override
  protected boolean parseLocationField(String field, Data data) {
    Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
    if (match.matches()) {
      field = match.group(1).trim();
      data.strState = match.group(2);
    }
    return super.parseLocationField(field, data);
  }

  @Override
  protected boolean parseMessageField(String field, Data data) {
    
    data.strSupp = field;
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ST";
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      "JACKSON COUNTY",
      
  //Cities

      "BRIDGEPORT",
      "SCOTTSBORO",
      "STEVENSON",

  //Towns

      "DUTTON",
      "HOLLYWOOD",
      "HYTOP",
      "LANGSTON",
      "PAINT ROCK",
      "PISGAH",
      "PLEASANT GROVES",
      "SECTION",
      "SKYLINE",
      "WOODVILLE",

  //Unincorporated Communities

      "BAILEYTOWN",
      "BASS",
      "BOLIVAR",
      "BRYANT",
      "CARD SWITCH",
      "ESTILLFORK",
      "FACKLER",
      "FLAT ROCK",
      "FRANCISCO",
      "GORHAM'S BLUFF",
      "HIGDON",
      "HOLLYTREE",
      "LARKIN",
      "LIBERTY HILL",
      "LIM ROCK",
      "LONG ISLAND",
      "PIKEVILLE",
      "PRINCETON",
      "RASH",
      "ROSALIE",
      "SWAIM",
      "TRENTON",
      
  //Ghost towns

      "BELLEFONTE",
      "LITTLE NASHVILLE"

  };
}
