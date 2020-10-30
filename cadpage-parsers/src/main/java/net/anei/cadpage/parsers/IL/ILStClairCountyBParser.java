package net.anei.cadpage.parsers.IL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILStClairCountyBParser extends FieldProgramParser {
  
  public ILStClairCountyBParser() {
    super("ST CLAIR COUNTY", "IL",
          "CALL:CALL! CALL/S+ PLACE:PLACE! PLACE+ ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATETIME! INFO:INFO! INFO/S+");
  }
   
  @Override
  public String getFilter() {
    return "FAIRVIEWHEIGHTS@PUBLICSAFETYSOFTWARE.NET,OFALLON@itiusa.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replaceAll("  +", " ");
    return parseFields(body.split("\n"), 7, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, " ", field);
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
}
