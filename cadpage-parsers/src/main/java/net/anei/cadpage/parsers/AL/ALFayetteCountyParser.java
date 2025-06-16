package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALFayetteCountyParser extends FieldProgramParser {

  public ALFayetteCountyParser() {
    super("FAYETTE COUNTY", "AL",
          "CALL! Comment:INFO! Address:ADDRCITY! GPS! Begin_Time:DATETIME! Call_#:ID! END");
  }

  @Override
  public String getFilter() {
    return "fayette.al@ryzyliant.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("OPS Broadcast:")) return false;
    return parseFields(body.split("\n"), data);
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/DD/YYYY hh:mm:ss aa");

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("On-dispatch OPS broadcast for '(.*)'", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("N/A")) return;
      super.parse(field, data);
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (data.strCity.equals("city N/A")) data.strCity = "";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("https?://.*query=(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Location coordinates not supplied")) return;
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim(), data);
    }
  }
}
