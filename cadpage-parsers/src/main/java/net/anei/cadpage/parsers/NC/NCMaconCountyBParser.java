package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCMaconCountyBParser extends FieldProgramParser {

  public NCMaconCountyBParser() {
    super("MACON COUNTY", "NC",
          "ID CODE_CALL ADDRCITY! GPS? PHONE NAME INFO/N+");
  }

  @Override
  public String getFilter() {
    return "4702193684,2183500429";
  }

  private static final Pattern INFO_MARK_PTN = Pattern.compile("\n\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d (?:[AP]M )? *");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, "\nStop");
    body = stripFieldEnd(body, "~");
    int tlen;
    do {
      tlen = body.length();
      body = stripFieldEnd(body, "\nnull");
      body = stripFieldEnd(body, "\nn");
    } while (body.length() < tlen);

    String info = "";
    Matcher match = INFO_MARK_PTN.matcher(body);
    if (match.find()) {
      info = body.substring(match.end()).trim();
      body = body.substring(0,match.start()).trim();
    }

    if (!parseFields(body.split("\n"), data)) return false;

    data.strSupp = append(data.strSupp, "\n", INFO_MARK_PTN.matcher(info).replaceAll("\n"));
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC DATE TIME " + super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}", true);
    if (name.equals("CODE_CALL")) return new MyCallCodeField();
    if (name.equals("GPS")) return new GPSField("\\d{2}\\.\\d{6,} -\\d{2}\\.\\d{6,}", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }

  private class MyCallCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = stripFieldEnd(field.substring(pt+3).trim(), " REPORTED AT");
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -")) return;
      super.parse(field, data);
    }
  }
}
