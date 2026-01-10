package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILPikeCountyParser extends FieldProgramParser {

  public ILPikeCountyParser() {
    super("PIKE COUNTY", "IL",
          "CALL_ID ADDRCITYST! Caller:NAME? Callback_#:PHONE? INFO/N+");
    removeWords("ESTATES");
  }

  @Override
  public String getFilter() {
    return "lawmancm@idsapplications.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_ID")) return new MyCallIdField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CALL_ID_PTN = Pattern.compile("(.*?) +(\\d{4}-\\d{6})");
  private class MyCallIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1);
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CALL ID";
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM) +(.*)|(\\d+[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);
  private static final Pattern MILE_MARKER_PTN = Pattern.compile("(?:[NSEW]B|MM)\\b.*");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parseAddress(String field, Data data) {

      // we get called after city, state and zip have been stripped off
      Parser p = new Parser(field);
      String fld = p.getLast(',');
      Matcher match = ADDR_APT_PTN.matcher(fld);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = match.group(2);
        fld = p.getLast(',');
        super.parseAddress(fld, data);
        data.strPlace = p.get();
        data.strApt = append(data.strApt, "-", apt);
      } else {
        String fld3 = fld;
        String fld2 = p.getLast(',');
        String fld1 = p.get();
        if (!fld1.isEmpty()) {
          data.strPlace = append(fld1, " - ", fld3);
          super.parseAddress(fld2, data);
        } else if (!fld2.isEmpty()) {
          if (checkAddress(fld2) >= checkAddress(fld3)) {
            super.parseAddress(fld2, data);
            data.strPlace = fld3;
          } else {
            data.strPlace = fld2;
            super.parseAddress(fld3, data);
          }
        } else {
          super.parseAddress(fld3, data);
        }
      }
      if (!data.strPlace.isEmpty()) {
        if (MILE_MARKER_PTN.matcher(data.strPlace).matches()) {
          data.strAddress = append(data.strAddress, " ", data.strPlace);
          data.strPlace = "";
        }
        else if (isValidCrossStreet(data.strPlace)) {
          data.strCross = data.strPlace;
          data.strPlace = "";
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X " + super.getFieldNames();
    }
  }

  private Pattern INFO_JUNK_PTN = Pattern.compile("\\d+ +\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
