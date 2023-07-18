package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStLouisCountyIParser extends FieldProgramParser {

  public MOStLouisCountyIParser() {
    super("ST LOUIS COUNTY", "MO",
          "CALL:CALL! PLACE:PLACE! ( ADDR:ADDR! | ADDRESS:ADDR! ) CROSS_STREET:X! CITY:CITY! JUNK+? ( PRIORITY:PRI! | PRIORTY:PRI! ) DATE:DATE! UNIT:UNIT! INFO:INFO/N+",
          FLDPROG_IGNORE_CASE);
  }

  @Override
  public String getFilter() {
    return "CALLALERT@DESPERESMO.ORG,DESPERESDPSOT@OMNIGO.COM";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    data.strCity = data.strCity.replace(".", "");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("JUNK")) return new SkipField("[^:]*");
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern MISSBLANK_PTN = Pattern.compile("(\\d{2,})([A-Z].*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      Matcher match = MISSBLANK_PTN.matcher(field);
      if (match.matches()) field = match.group(1) + ' ' + match.group(2);
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = DATE_TIME_PTN.matcher(field);
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
}
