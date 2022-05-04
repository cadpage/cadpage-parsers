package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class NYRocklandCountyEParser extends FieldProgramParser {
  
  public NYRocklandCountyEParser() {
    super("ROCKLAND COUNTY", "NY", 
          "NAME NUM? ADDR PHONE CALL! INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "CRFirst@chvrm.com";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern MARKER = Pattern.compile("Unit (\\d+) = +");
  private static final Pattern TRAIL_URL_PTN = Pattern.compile("\\s+http://.*?(?:,(\\d{5}))?$");
  private static final Pattern TRAIL_STATS_PTN = Pattern.compile("[, ]+(F-\\d+, B-\\d+, D-\\d+)$");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.isEmpty()) return false;
    
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strUnit = match.group(1);
    body = body.substring(match.end());
    
    match = TRAIL_URL_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0, match.start());
      data.strCity = convertCodes(getOptGroup(match.group(1)), ZIP_CODE_TABLE);
    } else {
      data.expectMore = true;
    }
    
    String stats = "";
    match = TRAIL_STATS_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0,match.start());
      stats = match.group(1);
    }
    
    if (!parseFields(body.split(","), data)) return false;
    data.strCall = append(subject, " - ", data.strCall);
    data.strSupp = append(data.strSupp, "\n", stats);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram() + " CITY";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NUM")) return new MyNumberField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern NUMBER_PTN = Pattern.compile("\\d+");
  private class MyNumberField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!NUMBER_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strAddress = field;
    }

    @Override
    public String getFieldNames() {
      return "ADDR";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }
  
  private static final Properties ZIP_CODE_TABLE = buildCodeTable(new String[]{
      "10901", "SUFFERN",
      "10952", "MONSEY",
      "10977", "SPRING VALLEY"
  });
}
