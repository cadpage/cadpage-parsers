package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Anne Arundel County, MD (Fireblitz)
 */
public class MDAnneArundelCountyFireblitzParser extends FieldProgramParser {

  public MDAnneArundelCountyFireblitzParser() {
    super("ANNE ARUNDEL COUNTY", "MD", "MAP_CALL! Date_Time:DATETIME! Dispatch:SKIP! Assignment:UNIT! Location:ADDR! Printout:URL!");
  }
  
  @Override
  public String getFilter() {
    return "@fireblitz.com";
  }

  private static Pattern CALL_ID = Pattern.compile("\\d{9}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!CALL_ID.matcher(subject).matches()) return false;
    data.strCallId = subject;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MAP_CALL")) return new MyMapCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d{2}/\\d{2} \\d{2}:\\d{2}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static Pattern MAP_CALL = Pattern.compile("(\\d+-\\d+): *(.*)");
  private class MyMapCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher mat = MAP_CALL.matcher(field);
      if (!mat.matches()) abort();
      data.strMap = mat.group(1);
      data.strCall = mat.group(2);
    }

    @Override
    public String getFieldNames() {
      return "MAP CALL";
    }
  }
  
  private static Pattern ADDR_CROSS = Pattern.compile("(.*?) *\\((.*?)\\)?");
  private static Pattern APT_PLACE = Pattern.compile("([A-Z]) +(.*)");
  private class MyAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = ADDR_CROSS.matcher(field);
      if (mat.matches()) {
        field = mat.group(1);
        data.strCross = mat.group(2);
      }
      parseAddress(StartType.START_ADDR, field, data);
      String left = getLeft();
      if (left.length() <= 5) data.strApt = left;
      else {
        mat = APT_PLACE.matcher(left);
        if (mat.matches()) {
          data.strApt = mat.group(1);
          data.strPlace = mat.group(2);
        } else data.strPlace = left;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE X";
    }
  }
}
