package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class INLakeCountyCParser extends FieldProgramParser {

  public INLakeCountyCParser() {
    super("LAKE COUNTY", "IN",
          "CALL ADDRCITY CODE! Cross_Street:X! GPS! END");
  }

  @Override
  public String getFilter() {
    return "flex@lcec911.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:.* - )?([A-Z]{3,5})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strSource = match.group(1);
    if (!parseFields(body.split("\\|"), data)) return false;
    if (!data.strCode.isEmpty()) {
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call != null) data.strCall = call;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(':');
      data.strPlace = p.getLastOptional(';');
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return;
      field = stripFieldStart(field, "Between:");
      super.parse(field, data);
    }
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
