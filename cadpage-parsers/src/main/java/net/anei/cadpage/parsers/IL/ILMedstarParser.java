package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class ILMedstarParser extends DispatchProQAParser {
  
  public ILMedstarParser() {
    super(CITY_LIST, "", "IL", 
          "ADDR EXTRA+? PROQA_DET");
  }
  
  @Override
  public String getFilter() {
    return "emsdispatch@medstarems.net,MedStar,1410";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("ProQA comments:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body,  data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("PROQA_DET")) return new SkipField("<PROQA_DET>");
    return super.getField(name);
  }
  
  
  private static final Pattern APT_PAT = Pattern.compile("(?:RM|ROOM|APT|SUITE) .*|[-\\d ]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern INITIALS_PAT = Pattern.compile("[A-Z]{1,3}", Pattern.CASE_INSENSITIVE);
  private static final Pattern MAP_PAT = Pattern.compile("(.*?) +PG *(\\d+)");
  private static final Pattern CROSS_PAT = Pattern.compile(".*(?:&| AND ).*", Pattern.CASE_INSENSITIVE);
  private class MyExtraField extends Field {
    @Override
    public void parse(String field, Data data) {
      
      if (field.length() == 0) return;
      
      // See if it has a room signature
      if (APT_PAT.matcher(field).matches()) {
        data.strApt = append(data.strApt, "-", field);
        return;
      }
      
      // Short 3 character fields are asssumed to be dispatcher initials
      // and ignored
      if (data.strCity.length() == 0 && INITIALS_PAT.matcher(field).matches()) return;
      
      // Anything with a ' PG ' in it should be a county code and map page number
      Matcher match = MAP_PAT.matcher(field);
      if (match.matches()) {
        if (data.strCity.length() == 0) {
          data.strCity = convertCodes(match.group(1), COUNTY_CODES);
        }
        data.strMap = match.group(2);
        return;
      }
      
      // When we find a city field, everything that we have accumulated in
      // call or info is moved to place or cross
      if (isCity(field)) {
        if (field.equalsIgnoreCase("SHILO")) field = "SHILOH";
        data.strCity = field;
        field = append(data.strCall, " / ", data.strSupp);
        if (CROSS_PAT.matcher(data.strCall).matches()) {
          data.strCross = field;
        } else {
          data.strPlace = field;
        }
        data.strCall = data.strSupp = "";
        return;
      }
      
      // Otherwise field is saved in call or info
      if (data.strCall.length() == 0) {
        data.strCall = field;
      } else if (data.strSupp.length() > 0) {
        data.strSupp = data.strSupp + " / " + field;
      } else if (isCallExtend(data.strCall, field)) {
        data.strCall = data.strCall + "/" + field;
      } else {
        data.strSupp = field;
      }
    }
    
    private boolean isCallExtend(String call, String info) {
      call = call.toUpperCase();
      info = info.toUpperCase();
      if (call.endsWith(" C") && info.startsWith("B ")) return true;
      if (call.endsWith(" Y") && info.startsWith("O ")) return true;
      if (info.equals("SUICIDE ATTEMPT")) return true;
      if (info.equals("RAPE")) return true;
      if (info.startsWith("BACK INJURIES")) return true;
      if (info.startsWith("LACERATIONS")) return true;
      if (info.startsWith("FAINTING")) return true;
      return false;
    }

    @Override
    public String getFieldNames() {
      return "APT MAP X PLACE CITY CALL INFO";
    }
  }
  
  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "SCC", "ST CLAIR COUNTY"
  });
  
  private static final String[] CITY_LIST = new String[]{
      // Clinton County
      
      // Cities
      "BREESE",
      "CARLYLE",
      "CENTRALIA",
      "TRENTON",
      // Villages
      "ALBERS",
      "AVISTON",
      "BARTELSO",
      "BECKEMEYER",
      "DAMIANSVILLE",
      "GERMANTOWN",
      "HOFFMAN",
      "KEYESPORT",
      "HUEY",
      "NEW BADEN",
      "ST ROSE",
      // Townships
      "BREESE",
      "BROOKSIDE",
      "CARLYLE",
      "CLEMENT",
      "EAST FORK",
      "GERMANTOWN",
      "IRISHTOWN",
      "LAKE",
      "LOOKING GLASS",
      "MERIDIAN",
      "SAINT ROSE",
      "SANTA FE",
      "SUGAR CREEK",
      "WADE",
      "WHEATFIELD",
      
      // St Clair County
      
      // Cities and Towns
      "ALORTON",
      "BELLEVILLE",
      "BROOKLYN",
      "CAHOKIA",
      "CASEYVILLE",
      "CENTREVILLE",
      "COLLINSVILLE",
      "DUPO",
      "EAST CARONDELET",
      "EAST ST LOUIS",
      "EAST SAINT LOUIS",
      "FAIRMONT CITY",
      "FAIRVIEW HEIGHTS",
      "FAYETTEVILLE",
      "FREEBURG",
      "LEBANON",
      "LENZBURG",
      "MARISSA",
      "MASCOUTAH",
      "MILLSTADT",
      "NEW ATHENS",
      "O'FALLON",
      "SAUGET",
      "SCOTT AFB",
      "SHILOH",
      "SHILO",    // Dispatch typo
      "SMITHTON",
      "ST. LIBORY",
      "SUMMERFIELD",
      "SWANSEA",
      "WASHINGTON PARK",
      // Townships
      "BELLEVILLE",
      "CANTEEN",
      "CASEYVILLE",
      "CENTREVILLE",
      "ENGELMANN",
      "FAYETTEVILLE",
      "FREEBURG",
      "LEBANON",
      "LENZBURG",
      "MARISSA",
      "MASCOUTAH",
      "MILLSTADT",
      "NEW ATHENS",
      "O'FALLON",
      "PRAIRIE DU LONG",
      "ST CLAIR",
      "SHILOH VALLEY",
      "SMITHTON",
      "STITES",
      "STOOKEY",
      "SUGARLOAF",
      
      // Randolf County
      
      // Cities and Towns
      "BALDWIN",
      "CHESTER",
      "COULTERVILLE",
      "ELLIS GROVE",
      "EVANSVILLE",
      "KASKASKIA",
      "PERCY",
      "PRAIRIE DU ROCHER",
      "RED BUD",
      "ROCKWOOD",
      "RUMA",
      "SPARTA",
      "STEELEVILLE",
      "TILDEN",
      // Unincorprated communities
      "GLENN",
      "GRIGG",
      "MENARD",
      "MODOC",
      "SCHULINE",
      "WALSH",
      "WELGE",
      "WINE HILL"
  };
}
