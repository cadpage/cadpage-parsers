package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class TXRockwallCountyEParser extends DispatchH05Parser {

  public TXRockwallCountyEParser() {
    this("ROCKWALL COUNTY", "TX");
  }

  public TXRockwallCountyEParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "( Units_Assigned:UNIT! | UnitAssigned:UNIT! ) Call_Type:CALL! Business_Name:PLACE! Location:ADDRCITY! Cross_Streets:X! Box:BOX! Narrative:EMPTY! INFO_BLK+");
  }

  @Override
  public String getAliasCode() {
    return "TXRockwallCountyE";
  }

  @Override
  public String getFilter() {
    return "donotreply@rockwallcountytexas.com,cadpagingalerts@rockwallcountytexas.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("This is an automated message");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("[A-Z]|.*\\d.*|");
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (APT_PTN.matcher(city).matches()) {
        apt = city;
        city = p.getLastOptional(',');
      }

      data.strCity = city;
      String addr = p.get();
      if (!apt.isEmpty()) addr = stripFieldEnd(addr, ' '+apt);
      int flags = FLAG_RECHECK_APT| FLAG_ANCHOR_END;
      if (!data.strCity.isEmpty()) flags |= FLAG_NO_CITY;
      addr = addr.replace('@', '&');
      parseAddress(StartType.START_ADDR, flags, addr, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private static final String[] CITY_LIST = new String[] {
      "DALLAS",
      "FATE",
      "GARLAND",
      "HEATH",
      "MCLENDON CHISHOLM",
      "MCLENDON-CHISHOLM",
      "MOBILE CITY",
      "ROCKWALL",
      "ROWLETT",
      "ROYSE CITY",
      "WYLIE",

      "COLLIN COUNTY",
      "HUNT COUNTY",
      "ROCKWALL COUNTY"
  };
}
