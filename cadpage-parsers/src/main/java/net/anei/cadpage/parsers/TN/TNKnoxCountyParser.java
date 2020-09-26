package net.anei.cadpage.parsers.TN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;


public class TNKnoxCountyParser extends FieldProgramParser {
  
  public TNKnoxCountyParser() {
    super(CITY_CODES, "KNOX COUNTY", "TN",
          "Location:ADDR/S! ( EID:ID! TYPE_CODE:CODE! TIME:TIME! CALLER_NAME:NAME! CALLER_ADDR:SKIP! SUB_TYPE:CALL% Disp:UNIT " + 
                           "| Xstreet1:X! Xstreet2:X! TYPE_CODE:CODE! Event_Description:CALL? SUB_TYPE:CODE/SDS% TIME:TIME% ) END");
    
  }
  
  @Override
  public String getFilter() {
    return "ipage@knox911.org";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean revMsgOrder() { return true; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 160; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Xstreet1:|Xstreet2:|TYPE CODE:|SUB TYPE:|TIME:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strAddress.equals("/")) {
      data.strAddress = data.strCross;
      data.strCross = "";
    }
    
    if (data.strCall.length() == 0) {
      data.strCall = data.strCode;
      data.strCode = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE CALL");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }
  
  private static final Pattern EXTRA_DASH_PTN = Pattern.compile("(\\d+)- ");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      
      field = stripFieldEnd(field, ": EST");
      
      if (field.startsWith("/")) {
        data.strAddress = "/";
        data.strCity = convertCodes(field.substring(1).trim(), CITY_CODES);
      } else {
        field = EXTRA_DASH_PTN.matcher(field).replaceAll("$1 ");
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
  public class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      // If we do not have a full length field, assume it has been truncated
      // and might be contained in a subsequent alert
      if (field.length() < 8) {
        data.expectMore = true;
        return;
      }
      if (!TIME_PTN.matcher(field).matches()) abort();
      super.parse(field, data);
    }
  }
  
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "BAKERTOWN",            "KNOXVILLE",
      "BALL CAMP",            "KNOXVILLE",
      "BLUEGRASS",            "KNOXVILLE",
      "CAMPBELL STATION",     "KNOXVILLE",
      "CEDAR BLUFF",          "KNOXVILLE",
      "CHOTO",                "KNOXVILLE",
      "CORRYTON",             "KNOXVILLE",
      "EBENEZER",             "KNOXVILLE",
      "FORKS OF THE RIVER",   "KNOXVILLE",
      "GALLAHER VIEW",        "KNOXVILLE",
      "GALLAHER VIEW",        "KNOXVILLE",
      "GIBBS",                "KNOXVILLE",
      "HALLS",                "KNOXVILLE",
      "HEISKELL",             "KNOXVILLE",
      "KARNS",                "KNOXVILLE",
      "KVFD",                 "KNOXVILLE",
      "LYONS VIEW",           "KNOXVILLE",
      "MALONEY",              "KNOXVILLE",
      "MARTIN MILL",          "KNOXVILLE",
      "MASCOT",               "KNOXVILLE",
      "MIDDLEBROOK",          "KNOXVILLE",
      "MIDWAY",               "KNOXVILLE",
      "MILLERTOWN",           "KNOXVILLE",
      "MORRELL",              "KNOXVILLE",
      "PELLISSIPPI",          "KNOXVILLE",
      "POWELL",               "KNOXVILLE",
      "STRAWBERRY PLAINS",    "KNOXVILLE",
      "SEYMOUR",              "KNOXVILLE",
      "TEDFORD",              "KNOXVILLE",
      "THORN GROVE",          "KNOXVILLE",
      "TOPSIDE",              "KNOXVILLE",
      "WATT",                 "KNOXVILLE",
      "WESTLAND",             "KNOXVILLE"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAKE", "BAKERTOWN/KNOXVILLE",
      "BALL", "BALL CAMP/KNOXVILLE",
      "BC" ,  "BLOUNT COUNTY",
      "BLUE", "BLUEGRASS/KNOXVILLE",
      "CAMP", "CAMPBELL STATION/KNOXVILLE",
      "CEDA", "CEDAR BLUFF/KNOXVILLE",
      "CHOT", "CHOTO/KNOXVILLE",
      "CORY", "CORRYTON/KNOXVILLE",
      "EBEN", "EBENEZER/KNOXVILLE",
      "FARR", "FARRAGUT",
      "FORK", "FORKS OF THE RIVER/KNOXVILLE",
      "GAL",  "GALLAHER VIEW/KNOXVILLE",
      "GALL", "GALLAHER VIEW/KNOXVILLE",
      "GIBB", "GIBBS/KNOXVILLE",
      "HALL", "HALLS/KNOXVILLE",
      "HEIS", "HEISKELL/KNOXVILLE",
      "KARN", "KARNS/KNOXVILLE",
      "KELL", "KELLER BEND",
      "KNOX", "KNOXVILLE",
      "KVFD", "KVFD/KNOXVILLE",
      "LYON", "LYONS VIEW/KNOXVILLE",
      "MALY", "MALONEY/KNOXVILLE",
      "MART", "MARTIN MILL/KNOXVILLE",
      "MASC", "MASCOT/KNOXVILLE",
      "MIDD", "MIDDLEBROOK/KNOXVILLE",
      "MIDW", "MIDWAY/KNOXVILLE",
      "MILL", "MILLERTOWN/KNOXVILLE",
      "MORR", "MORRELL/KNOXVILLE",
      "PELL", "PELLISSIPPI/KNOXVILLE",
      "POWL", "POWELL/KNOXVILLE",
      "STRA", "STRAWBERRY PLAINS/KNOXVILLE",
      "SVFD", "SEYMOUR/KNOXVILLE",
      "TEDF", "TEDFORD/KNOXVILLE",
      "THOR", "THORN GROVE/KNOXVILLE",
      "TOPS", "TOPSIDE/KNOXVILLE",
      "WATT", "WATT/KNOXVILLE",
      "WEST", "WESTLAND/KNOXVILLE"
  });
}
