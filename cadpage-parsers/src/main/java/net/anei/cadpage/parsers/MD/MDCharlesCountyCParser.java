package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDCharlesCountyCParser extends FieldProgramParser {

  public MDCharlesCountyCParser() {
    super("CHARLES COUNTY", "MD", "CALL ADDR! DISTRICT:MAP! TG:MAP! ID! DATETIME! Notes:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "sms.dispatch@ccmedicunit.org";
  }
  
  @Override
  public int getMapFlags() {
    //suppress LA->LN to support "LA PLATA RD"
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("ID")) return new IdField("\\d+", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static Pattern PRI_CALL = Pattern.compile("PRIORITY (\\d+) (.*)");
  public class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = PRI_CALL.matcher(field);
      if (mat.matches()) {
        data.strPriority = mat.group(1);
        super.parse(mat.group(2), data);
      } else super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PRI " + super.getFieldNames();
    }
  }
  
  //split up and nested the pattern to reduce complexity
  private static Pattern ADDR_PLACE = Pattern.compile("(.*?), +(.*)");
  private static Pattern HEAD_ST_ZIP = Pattern.compile("(.*) +([A-Z]{2}) +(\\d{5})");
  private static Pattern UNIT = Pattern.compile("UNIT ([^ ]+)(?: +(.*))?");
  public class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      //parse trailing ST ZIP
      Matcher mat = HEAD_ST_ZIP.matcher(field);
      if (mat.matches()) {
        field = mat.group(1);
        data.strState = getOptGroup(mat.group(2));
        data.strCity = getOptGroup(mat.group(3));
      } //parse ADDR, UNIT
      mat = ADDR_PLACE.matcher(field);
      if (mat.matches()) {
        field = mat.group(1);
        //g2 is UNIT and/or PLACE
        String g2 = mat.group(2);
        mat = UNIT.matcher(g2);
        if (mat.matches()) {
          data.strApt = mat.group(1);
          data.strPlace = getOptGroup(mat.group(2));
        } else data.strPlace = g2;
      } super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " APT PLACE CITY ST";
    }
  }
  
  public class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      //replace blanks and append to strMap with dashes
      data.strMap = append(data.strMap, "-", field.replace(" ", "-"));
    }
  }
  
  public class MyDateTimeField extends DateTimeField {
    public MyDateTimeField() {
      //validation pattern
      super("\\d{2}\\.\\d{2}\\.\\d{2} \\d{2}:\\d{2}", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      //replace . with / and parse normally
      super.parse(field.replace('.', '/'), data);
    }
  }
}
