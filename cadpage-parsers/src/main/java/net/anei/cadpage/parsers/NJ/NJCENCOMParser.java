package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * CENCOM, NJ
 * Covers parts of
 *   Sussex County
 *   Warren County
 *   Pike County, PA
 */


public class NJCENCOMParser extends FieldProgramParser {

  public NJCENCOMParser() {
    this("", "NJ");
  }

  protected NJCENCOMParser(String defCity, String defState) {
    super(defCity, defState,
           "Unit:MAIN! Cross_St:X Pickup_Location:PLACE Pt_Name:NAME");
  }
  
  @Override
  public String getFilter() {
    return "cencom@ahsys.org";
  }
  
  @Override
  public String getLocName() {
    return "CENCOM (Central NJ), NJ";
  }
  
  @Override
  public String getAliasCode() {
    return "NJCENCOM";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = RUN_RPT_MASTER.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      data.strPlace = match.group(3);
      return true;
    }
    return super.parseMsg(body, data);
  }
  private static final Pattern RUN_RPT_MASTER = Pattern.compile("Unit ([-A-Z0-9]+?) +# *(\\d{4}-\\d{6,}) (Recv'd .*)");
  
  private static final Pattern MAIN_MASTER = Pattern.compile("([-A-Z0-9]+?) +#(\\d{4}-\\d{6,}) +(.*? [AB]LS) +(.*? (?:Twp|Town|Boro)) +(.*?)(?:\\b(?:Room|Apt)(.*))?");
  private class MainField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAIN_MASTER.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      data.strCall = match.group(3).trim();
      data.strCity = match.group(4).trim();
      String sAddr = match.group(5).trim();
      data.strApt = getOptGroup(match.group(6));

      if (sAddr.startsWith("(Pike)")) {
        data.strState = "PA";
        if (data.strCity.length() == 0) data.strCity = "PIKE COUNTY";
        sAddr = sAddr.substring(6).trim();
      }
      int pt = sAddr.indexOf('(');
      if (pt >= 0) {
        sAddr = sAddr.substring(pt+1).trim();
        pt = sAddr.indexOf(')');
        if (pt >= 0) sAddr = sAddr.substring(0,pt).trim(); 
      }
      parseAddress(sAddr, data);
      
      String upCity = data.strCity.toUpperCase();
      if (upCity.endsWith(" BORO")) {
        data.strCity = data.strCity.substring(0, data.strCity.length()-5).trim();
      }
      if (upCity.startsWith("DINGMAN") || upCity.startsWith("WESTFALL") ||
          upCity.startsWith("MATAMORAS")) data.strState = "PA";
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT ID CALL CITY ST ADDR APT";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAIN")) return new MainField();
    return super.getField(name);
  }
}
