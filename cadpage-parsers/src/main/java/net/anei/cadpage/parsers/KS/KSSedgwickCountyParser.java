package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


public class KSSedgwickCountyParser extends DispatchH05Parser {

  public KSSedgwickCountyParser() {
    super("SEDGWICK COUNTY", "KS",
          "PLACE:PLACE! ADDR:ADDRCITY! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY! INFO_BLK/N+");
  }

  @Override
  public String getFilter() {
    return "911_notify@sedgwick.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CITY_APT_PTN = Pattern.compile("(?:([A-Z ]+?) +)??(?:(?:APT|#) *(.*)|([A-Z]?\\d+[A-Z]?|[A-Z]|))", Pattern.CASE_INSENSITIVE);

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = CITY_APT_PTN.matcher(city);
      String apt = "";
      if (match.matches()) {
        city = match.group(1);
        apt = match.group(2);
        if (apt == null) apt = match.group(3);
        if (city == null) city = p.getLastOptional(',');
      }
      data.strCity = city;

      field = p.get().replace('@', '&');
      parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      if (apt.contains(data.strApt)) {
        data.strApt = apt;
      } else if (!data.strApt.contains(apt)) {
        data.strApt = append(data.strApt, "-", apt);
      }
    }
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.equals("HWY")) return true;
    return super.isNotExtraApt(apt);
  }

}
