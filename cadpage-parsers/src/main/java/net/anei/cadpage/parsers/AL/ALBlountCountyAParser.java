package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;



public class ALBlountCountyAParser extends DispatchA71Parser {

  public ALBlountCountyAParser() {
    super("BLOUNT COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@blount911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern CROSS_INFO_PTN = Pattern.compile("([^a-z]*?)(?:^(?=\\d+ *[A-Z]?[a-z])| (?=[A-Z][a-z])|(?<=[A-Z])(?=[1-9])|(?=[a-z]))");
  private static final Pattern CROSS_INFO_PTN2 = Pattern.compile("([^a-z]*? @ [^a-z]*? (?:AVE|BLVD|CIR|CT|DR|LN|LOOP|RD|ST|TRL|TR(?!EE|OT)|HWY \\d+))");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCross.isEmpty()) {
      boolean found = true;
      String info = data.strSupp.replace(" (Verify) ", " ");
      Matcher match = CROSS_INFO_PTN.matcher(info);
      if (!match.lookingAt()) {
        match = CROSS_INFO_PTN2.matcher(info);
        if (!match.lookingAt()) found = false;
      }
      if (found) {
        data.strCross = match.group().trim().replace('@', '/').replace("*", "");
        data.strSupp = data.strSupp.substring(match.end(1)).trim();
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("INFO", "X? INFO");
  }




//
//  private static final Pattern DELIM = Pattern.compile(",?\n");
//
//  @Override
//  protected boolean parseMsg(String body, Data data) {
//    body = stripFieldEnd(body, ",");
//    return parseFields(DELIM.split(body), data);
//  }
//
//  @Override
//  public Field getField(String name) {
//    if (name.equals("X")) return new MyCrossField();
//    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
//    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
//    if (name.equals("XINFO")) return new MyCrossInfoField();
//    return super.getField(name);
//  }
//
//  private class MyCrossField extends CrossField {
//    @Override
//    public void parse(String field, Data data) {
//      field = field.replace('@',  '/').replace("*", "");
//      super.parse(field, data);
//    }
//  }
//
//  private class MyCrossInfoField extends MyCrossField {
//    @Override
//    public void parse(String field, Data data) {
//      Matcher match = CROSS_INFO_PTN.matcher(field);
//      if (match.matches()) {
//        field = match.group(1).trim();
//        data.strSupp = match.group(2);
//      }
//      super.parse(field, data);
//    }
//
//    @Override
//    public String getFieldNames() {
//      return "X INFO";
//    }
//  }
}
