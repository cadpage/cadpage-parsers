package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PASomersetCountyCParser extends FieldProgramParser {

  public PASomersetCountyCParser() {
    super("SOMERSET COUNTY", "PA",
          "SRC? CFS_#:ID? Date/Time:DATETIME? Call_Type:CALL! Location:ADDRCITY! Common_Name:PLACE? Additional_Location:APT_PLACE! Cross_Streets:X? Common_Name:PLACE? Caller_Phone:PHONE! Caller_Name:NAME! Units:UNIT! Talkgroup:CH! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatch@fcema.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch|Somerset 911")) return false;
    return parseFields(body.split("\n"), data);
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("(.*?) +Page", true);
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:apt|room|suite|lot) +(.*)|\\d{1,3}|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
      }

      else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}
