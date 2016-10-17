package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXCarrolltonParser extends FieldProgramParser {

  public TXCarrolltonParser() {
    super(CITY_CODES, "CARROLLTON", "TX", "CALL:ID! NAT:CALL! DC:DCMAP! LOC:ADDR/y! APT! GRID! UNITS!");
  }

  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split(","), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DCMAP")) return new MyMapField();
    if (name.equals("APT")) return new AptField("APT ?(.*)");
    if (name.equals("GRID")) return new GridField();
    if (name.equals("UNITS")) return new UnitField("UNITS ?(.*)", true);
    return super.getField(name);
  }

  public class GridField extends MapField {
    public GridField() {
      super("GRID ?(.*)", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, " / ", field);
    }
  }

  private static Pattern DCMAP = Pattern.compile("(.*?) ?MAP: ?(.*)");

  public class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = DCMAP.matcher(field);
      if (!mat.matches()) abort();
      data.strMap = append(mat.group(1), " / ", mat.group(2));
    }
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "AD", "ADDISON",
      "CA", "CARROLLTON",
      "CO", "COPPEL",
      "DA", "DALLAS",
      "FB", "FARMERS BRANCH",
      "PL", "PLANO",
      "LV", "LEVWISVILLE",
      "TC", "THE COLONY",
      "FM", "FLOWER MOUND",
  });

}
