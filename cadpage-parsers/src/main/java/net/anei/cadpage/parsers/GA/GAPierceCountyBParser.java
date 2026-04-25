package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class GAPierceCountyBParser extends FieldProgramParser {

  public GAPierceCountyBParser() {
    super("PIERCE COUNTY", "GA",
          "CadCallCaseNo:ID! IncidentLocation:PLACE!  IncidentAddNo:ADDR1 IncidentStreet:ADDR2 IncidentCity:CITY PrimUnitNo:UNIT! " +
              "DispatchTime:DISPATCH_TIME! OnSceneTime:ONSCENE_TIME! ClearTime:CLEAR_TIME! " +
              "IncidentType:CALL! IncidentComment:INFO! IncidentLat:GPS1 IncidentLon:GPS2 StationId:SRC! FirstName:NAME LastName:NAME/S Phone:PHONE",
          FLDPROG_XML);
  }

  @Override
  public String getFilter() {
    return "pierce.ga@ez911map.net,pierce.ga@ryzyliant.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String times =  null;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    times = "";
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strAddress.isEmpty() && !data.strPlace.isEmpty()) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }

    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(times, "\n", data.strSupp);
    }
    times = null;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("DISPATCH_TIME")) return new TimesField(1, "Dispatched");
    if (name.equals("ONSCENE_TIME")) return new TimesField(2, "On Scene");
    if (name.equals("CLEAR_TIME")) return new TimesField(3, "Cleared");
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      String[] flds = field.split(" *\\| *");
      data.strPlace = flds[0];
      if (flds.length >= 2) data.strApt = flds[1];
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT ADDR?";
    }
  }

  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = field;
    }
  }

  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");

  private class TimesField extends Field {
    int type;
    private String title;

    public TimesField(int type, String title) {
      this.type = type;
      this.title = title;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (!match.matches()) abort();

      String date =  match.group(1);
      if (date.equals("01/01/1800")) return;

      String time = match.group(2);

      if (type == 1) {
        data.strDate = date;
        data.strTime = time;
      }

      times = append(times, "\n", time + " " + title);

      if (type == 2) data.msgType = MsgType.RUN_REPORT;
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
}
