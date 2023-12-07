package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PAPerryCountyParser extends DispatchH05Parser {

  public PAPerryCountyParser() {
    super(CITY_LIST, "PERRY COUNTY", "PA",
          "( SELECT/1 CALL:CALL! PLACE:PLACE! ADDR:ADDRCITYGPS1/S6! " +
                 "( CRSS:X! MAP:GPS! CFS:SKIP! ID:ID! PRI:PRI1! DATE:DATETIME! UNIT:UNIT! TIMES:EMPTY! TIMES+ " +
                 "| CROSS_STREETS:X! ID:ID! PRI:PRI1! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY! TIMES+? CALLER_NAME:NAME! FIRE_BOX:BOX! END ) " +
          "| CALL_ADDR_CITY2 X PLACE APT GPS! PN:PHONE! MAP UNIT! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "noreply@jcpc911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.equals("Do Not Reply")) {
      setSelectValue("2");
      return parseFields(body.split("\n"), data);
    } else {
      setSelectValue("1");
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYGPS1")) return new MyAddressCityGPS1Field();
    if (name.equals("PRI1")) return new MyPriority1Field();

    if (name.equals("CALL_ADDR_CITY2")) return new MyCallAddressCity2Field();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("(\\d+\\.\\d{6,})(-\\d+\\.\\d{6,})");
  private static final Pattern APT_PTN = Pattern.compile(".*\\d.*|[A-Z]");

  private class MyAddressCityGPS1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.isEmpty()) city = p.getLastOptional(',');
      Matcher match = GPS_PTN.matcher(city);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        city = p.getLastOptional(',');
      }
      String apt = "";
      match = APT_PTN.matcher(city);
      if (match.matches()) {
        apt = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      field = p.get().replace('@', '&');
      if (!apt.isEmpty()) {
        field = stripFieldEnd(field, ' ' + apt);
      }
      int flags = FLAG_ANCHOR_END | FLAG_RECHECK_APT;
      if (!data.strCity.isEmpty()) flags |= FLAG_NO_CITY;
      parseAddress(StartType.START_ADDR, flags, field, data);
      if (!apt.equals(data.strApt)) {
        data.strApt = append(data.strApt, "-", apt);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT GPS";
    }
  }

  private class MyPriority1Field extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('-');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyCallAddressCity2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt < 0) abort();
      data.strCall = field.substring(0,pt).trim();
      super.parse(field.substring(pt+1).trim(), data);
    }
  }

  // Only out of county locations need go here
  private static final String[] CITY_LIST = new String[] {
      "COAL TWP"
  };
}
