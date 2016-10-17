package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAHenryCountyParser extends FieldProgramParser {
  
  public GAHenryCountyParser() {
    super("HENRY COUNTY", "GA",
           "CALL+? ADDR! INFO+? UNIT TIME");
  }
  
  @Override
  public String getFilter() {
    return "93001,777";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("/"), data);
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCall.length() > 4) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, "/", field);
    }
  }
  
  private class MyAddressField extends AddressField{
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" INTERSECTN")) field = field.substring(0, field.length()-11).trim();
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "/", field);
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return DIR_HWY_PTN.matcher(address).replaceAll("$1");
  }
  private static final Pattern DIR_HWY_PTN = Pattern.compile("[NSEW] +((?:HIGHWAY|HWY|US) +\\d+)", Pattern.CASE_INSENSITIVE);
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
