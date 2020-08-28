package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXWilliamsonCountyParser extends DispatchOSSIParser {

  public TXWilliamsonCountyParser() {
    super(CITY_CODES, "WILLIAMSON COUNTY", "TX", 
          "( CANCEL! | FYI? CALL! ) PRI? ADDR/S! CITY? PLACE_MAP? UNIT/C+");
  }
  
  @Override protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.equals("Message Forwarded by PageGate") &&
        !body.startsWith("CAD:")) body = "CAD:" + body;
    
    if (body.startsWith("CAD:ATTN ")) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body.substring(4);
      return true;
    }
    
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("[P0-9]");
    if (name.equals("PLACE_MAP")) return new MyPlaceMapField();
    return super.getField(name);
  }
  
  private static Pattern PLACE_MAP_PAT = Pattern.compile("[.\\\\]?(?:(.*)\\(S\\)(.*?) )?\\(N\\)(.*?)");
  private class MyPlaceMapField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher mat = PLACE_MAP_PAT.matcher(field);
      if (!mat.matches()) return false;
      data.strPlace = append(getOptGroup(mat.group(1)), " - ", getOptGroup(mat.group(2)));
      data.strMap = mat.group(3).trim();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "PLACE MAP";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return PVR_PTN.matcher(addr).replaceAll("PVT RD");
  }
  private static final Pattern PVR_PTN = Pattern.compile("\\bPVR\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AU",      "AUSTIN",
      "BT",      "BARTLETT",
      "CL",      "COUPLAND",
      "CP",      "CEDAR PARK",
      "EL",      "ELGIN",
      "FL",      "FLORENCE",
      "GR",      "GRANGER",
      "GT",      "GEORGETOWN",
      "HU",      "HUTO",
      "JA",      "JARRELL",
      "LE",      "LEANDER",
      "LH",      "LIBERTY HILL",
      "RR",      "ROUND ROCK",
      "TA",      "TAYLOR",
      "TD",      "THORNDALE",
      "TH",      "THRALL",
      "WR",      "WEIR"
  });
}
