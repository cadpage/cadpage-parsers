package net.anei.cadpage.parsers.KS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KSWyandotteCountyParser extends DispatchH05Parser {

  public KSWyandotteCountyParser() {
    super("WYANDOTTE COUNTY", "KS",
          "( CALL:CALL! INCIDENT:ID? PLACE:PLACE! ADDR:ADDRCITY! PRI:PRI! DATE:DATETIME! UNIT:UNIT! INFO:INFO_BLK+ Call_Date:SKIP " +
          "| SKIP+ CFS:SKIP! Incident_Number:ID! Call_Date:DATETIME! Type:CALL! Loc:SRC! ADDR_CITY_APT! Lat:GPS! Cross_Streets:X! Cmt:EMPTY! INFO_BLK+ Units:UNIT! Times:EMPTY! TIMES+ )");
  }

  @Override
  public String getFilter() {
    return "@kckfd.org,@kckpd.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return parseFields(body.split("\n"), data);
  }


  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDR_CITY_APT")) return new MyAddressCityAptField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(DATE_TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private class MyAddressCityAptField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String apt = p.getLastOptional('#');
      data.strCity = p.getLastOptional(',');
      field = p.get().replace('@', '&');
      field = stripFieldEnd(field, apt);
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }
}
