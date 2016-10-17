package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYDuchessCountyParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^([-A-Z0-9]+): ");

  public NYDuchessCountyParser() {
      super("DUCHESS COUNTY", "NY",
           "CALL! PLACE ADDR CALL! Cross:X INFO+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strSource = match.group(1);
    body = body.substring(match.end()).trim();
    return parseFields(body.split("\\|"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String state = p.getLastOptional(',');
      if (state.equals("NY") || state.equalsIgnoreCase("New York")) state = "";
      String city = p.getLastOptional(',');
      super.parse(p.get(), data);
      data.strCity = city;
      data.strState = state;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" - ", " / ");
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return TSP_PTN.matcher(addr).replaceAll("TACONIC STATE PKWY");
  }
  private static final Pattern TSP_PTN = Pattern.compile("\\bTSP\\b");
}
