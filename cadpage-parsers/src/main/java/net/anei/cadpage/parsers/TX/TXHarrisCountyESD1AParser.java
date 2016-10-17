package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class TXHarrisCountyESD1AParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile(" *(?: /)+ *| *(?:/ )+ *");

  public TXHarrisCountyESD1AParser() {
    super("HARRIS COUNTY", "TX",
           "ID? CODE? CALL ADDR! ADDR2 APT:APT! BLD:APT! KM:MAP XSTRTS:X Box:BOX");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("/")) body = body.substring(1).trim();
    body = body.replace("Box #:", "Box:");
    return parseFields(DELIM.split(body), data);
  }
  
  // ID Field has to fit specific pattern
  private class MyIdField extends IdField {
    public MyIdField() {
      setPattern(Pattern.compile("\\d\\d-\\d\\d-\\d{5}"));
    }
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
 
  // Code field has to fit specific pattern
  private class MyCodeField extends CodeField {
    public MyCodeField() {
      setPattern(Pattern.compile("\\d\\d[A-Z]\\d\\d[A-Za-z]?"));
    }
  }
  
  // ADDR2 is a second address field, intersections are reported as two
  // different fields that have to be put back together
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, " & ", field);
    }
  }
  
  // There are two fields, APT and BLD mapped to the apartment field.  But they
  // occur in reverse order, if both are specified, we want to to put bldg first.
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      data.strApt = append(field, "-", data.strApt);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }
  
}
