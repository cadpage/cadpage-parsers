package net.anei.cadpage.parsers.LA;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAIbervilleParishParser extends FieldProgramParser {

  public LAIbervilleParishParser() {
    super("IBERVILLE PARISH", "LA",
          "CALL CALL/SDS ADDRCITYST PLACE APT X INFO UNIT ID GPS1 GPS2! END");
  }

  @Override
  public String getFilter() {
    return "IbervilleOEP@iberville911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern DELIM = Pattern.compile(" *\\* +");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID")) return new IdField("CFS-\\d\\d-\\d+", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = field.replace("; ", " - ");
      super.parse(field, data);
    }
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = stripFieldStart(field, "APT ");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = field;
    }
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Set<String> srcSet = new HashSet<>();
      for (String unit : UNIT_BRK_PTN.split(field)) {
        int pt = unit.indexOf(" - ");
        if (pt >= 0) {
          String src = unit.substring(0,pt).trim();
          unit = unit.substring(pt+3).trim();
          if (srcSet.add(src)) data.strSource = append(data.strSource, ",", src);
        }
        unit = stripFieldEnd(unit, "-DISP");
        data.strUnit = append(data.strUnit, ",", unit);
      }
    }

    @Override
    public String getFieldNames() {
      return "SRC UNIT";
    }
  }
}
