package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class MDOceanCityParser extends DispatchOSSIParser {

  private String address1 = null;
  private String address2 = null;
  private int addrStat1 = -1;
  private int addrStat2 = -1;
  
  @Override
  public String getFilter() {
    return "CAD@mail.oceancitymd.gov";
  }
  
  public MDOceanCityParser() {
    super("OCEAN CITY", "MD",
          "( CANCEL ADDR! INFO+ | FYI ( DATETIME CALL ADDR! | CALL APT? ADDR/Z+? DATETIME! X+ ) )");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    address1 = null;
    address2 = null;
    addrStat1 = -1;
    addrStat2 = -1;
    
    if (! super.parseMsg(subject, body, data)) return false;
    
    // Pick the best address as the address
    // If no place name was found, use the second best address
    if (address1 == null) return false;
    parseAddress(address1, data);
    if (address2 != null) data.strPlace = address2;
    return true;
  }
  
  
  private Pattern APT_PTN = Pattern.compile("(?:UNIT|APT|ROOM|RM) +(.*)|(?:SUITE|LOT) +.*|.*\\b(?:FLOOR|FLR)\\b.*|[A-Z0-9]{1,3}", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (!match.matches()) return false;
      String apt = match.group(1);
      if (apt == null) apt = field;
      data.strApt = apt;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  // This text format has 1-3 address fields, and the real address might
  // be in either one.
  private Pattern DIR_PTN = Pattern.compile("\\([NS]\\)");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      int status = (DIR_PTN.matcher(field).find() ? -1 : checkAddress(field));
      if (address1 == null || status > addrStat1) {
        address2 = address1;
        addrStat2 = addrStat1;
        address1 = field;
        addrStat1 = status;
      } else if (address2 == null || status > addrStat2) {
        address2 = field;
        addrStat2 = status;
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL");
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
