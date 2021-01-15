package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA59Parser extends FieldProgramParser {

  public DispatchA59Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA59Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
           "EVENT:ID? TIME:DATETIME? TYPE:CALL/SDS? LOC:ADDR/S? TXT:INFO? INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if(!subject.equals("CAD")) return false;
    if (!parseFields(body.split("\n"), data)) return false;

    // Everything is optional, but we have to have *SOMETHING*
    return (data.strTime.length() > 0 || data.strCall.length() > 0 || data.strAddress.length() > 0);
  }

  @Override
  public Field getField(String  name) {
	  if (name.equals("ID")) return new IdField("\\d{4}",true);
	  if (name.equals("DATETIME")) return new BaseDateTimeField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(\\d{2}/\\d{2}/\\d{4})|([A-Z].*?|.* Page)) (\\d{2}:\\d{2}:\\d{2})");
  private class BaseDateTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      String date = match.group(1);
      if (date != null) {
        data.strDate = date;
      } else {
        data.strCall = match.group(2).trim();
      }
      data.strTime = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL?";
    }
  }
}
