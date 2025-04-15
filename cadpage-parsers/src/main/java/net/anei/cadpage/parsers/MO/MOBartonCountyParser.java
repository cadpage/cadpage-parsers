package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MOBartonCountyParser extends FieldProgramParser {

  public MOBartonCountyParser() {
    super("BARTON COUNTY", "MO",
          "Address:ADDRCITY! Intersection:X! Category:CALL! Sub_Category:CALL/SDS! Persons:NAME! NAME/CS+ Phone_Number:PHONE! " +
          "Notes:EMPTY! INFO/N+ Event_Number:ID! Originated_By:SKIP! Opened_Date_/_Time:DATETIME! MORE_INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("MORE_INFO")) return new MyMoreInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_UNIT_PTN = Pattern.compile("(.*?) *\\bUnit\\b *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR_UNIT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  private class MyMoreInfoField extends InfoField {
    private boolean skip = false;

    @Override
    public void parse(String field, Data data) {
      if (skip) {
        if (field.contains(":")) skip = false;
        else return;
      } else if (field.startsWith("Persons:")) {
        skip = true;
        return;
      }
      if (field.startsWith("Location:") ||
          field.startsWith("Event Number:") ||
          field.startsWith("Originated By:") ||
          field.startsWith("Opened Date / Time:") ||
          field.startsWith("Phone Number:") ||
          field.startsWith("Notes:")) return;

      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
