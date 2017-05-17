package net.anei.cadpage.parsers.ZCAON;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCAONChathamKentParser extends FieldProgramParser {
  
  public ZCAONChathamKentParser() {
    super("CHATHAM-KENT", "ON", 
          "CALL:CALL! ( PLACE:PLACE! ADDR:ADDR! XSTR:X CITY:CITY! ID:ID? | ) DATE:DATE! TIME:TIME! ( LATITUDE:GPS1! LONGITUDE:GPS2! ID:ID? UNITS:UNIT! SOURCE:SKIP! | ) INFO:INFO");
    addRoadSuffixTerms("LI");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern EOM_MARK_PTN = Pattern.compile(" Access Token Refreshed | CALLBACK ROSTER ASSIGNED:| DISPATCH TIME:| Dispath Time:|\n-{5,}\n");
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = EOM_MARK_PTN.matcher(body);
    if (match.find()) body = body.substring(0, match.start()).trim();
    if (!super.parseFields(body.split(";"), data)) return false;
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }
  
  private static final Pattern LI_PTN = Pattern.compile("\\bLI\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = LI_PTN.matcher(addr).replaceAll("LINE");
    return super.adjustMapAddress(addr);
  }
  
  @Override
  public String adjustMapCity(String city) {
    
    // Do not know what they are putting in the city code, but Google sure doesn't recognize it
    return "";
  }
}
