package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class GASchleyCountyParser extends FieldProgramParser {
  
  public GASchleyCountyParser() {
    this("SCHLEY COUNTY");
  }
  
  public GASchleyCountyParser(String defCounty) {
    super(defCounty, "GA",
          "SRC! ( RUN_REPORT LOC:ADDR! Phone:PHONE! Type:CALL! SUBTYPE:CALL2! CREATED:SKIP! " +  
               "| Type:CALL! SUBTYPE:CALL2! LOC:ADDR! ) C:EXTRA! END");
  }
  
  @Override
  public String getFilter() {
    return "MIDDLEFLINT@MFRE911.COM";
  }
  
  @Override
  public String getAliasCode() {
    return "GASchleyCounty";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("Times -")) data.msgType = MsgType.RUN_REPORT;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("RUN_REPORT")) return new MyRunReportField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("EXTRA")) return new MyExtraField();
    return super.getField(name);
  }
  
  private class MyRunReportField extends SelectField {
    @Override
    public boolean checkParse(String field, Data data) {
      return data.msgType == MsgType.RUN_REPORT;
    }
  }
  
  // Subtype field is appended to call description, unless it is a ?
  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("?") || field.equals("\ufffd")) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  // Address field may have appended place name
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get("[@"), data);
      data.strPlace = p.get('[');
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  // The C: field is a conglomeration of all kinds of things
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\b(\\d{1,2}/\\d{1,2}/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)");
  private static final Pattern DATE_TIME_TRAIL = Pattern.compile("\\b\\d{1,2}/[0-9/]+(?: +[0-9:]+)?$");
  private static final Pattern UNIT_PTN = Pattern.compile("\\b([A-Z][A-Z0-9]+) : ");
  private static final String CROSS_STREET_KEY = "cross streets:";
  private class MyExtraField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      
      // Break the field down by date/time separators, the first of which
      // be stored as the dispatch date/time
      Matcher match = DATE_TIME_PTN.matcher(field);
      int pt = 0;
      while (match.find()) {
        if (data.strDate.length() == 0) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
        }
        parseBlock(field.substring(pt, match.start()), data);
        pt = match.end();
      }
      
      // Strip out remains a partial date/time before processing what is left
      String left = field.substring(pt);
      match = DATE_TIME_TRAIL.matcher(left);
      if (match.find()) left = left.substring(0,match.start()).trim();
      parseBlock(left, data);
    }
    
    private void parseBlock(String block, Data data) {
      
      // If nothing here, skip it
      block = block.trim();
      if (block.length() == 0) return;
      
      // Look for a Unit
      Matcher match = UNIT_PTN.matcher(block);
      if (match.find()) {
        String left = block.substring(match.end());
        if (left.startsWith("DSPTCH") || "DSPTCH".startsWith(left)) {
          data.strUnit = append(data.strUnit, ",", match.group(1));
          block = block.substring(0,match.start()).trim();
        }
      }
      
      // Look for a cross street designator
      int pt = block.indexOf(CROSS_STREET_KEY);
      if (pt >= 0) {
        String sCross = block.substring(pt+CROSS_STREET_KEY.length()).trim();
        sCross = stripFieldStart(sCross, "/");
        sCross = stripFieldEnd(sCross, "/");
        data.strCross = append(data.strCross, " & ", sCross);
        block = block.substring(0,pt).trim();
      }
      
      // If we didn't find one, look for a partial cross street designator
      else {
        pt = block.lastIndexOf(' ');
        if (pt >= 0) {
          String left = block.substring(pt+1);
          if (CROSS_STREET_KEY.startsWith(left)) {
            block = block.substring(0,pt).trim();
          }
          else if (pt >= 6) {
            pt -= 6;
            left = block.substring(pt);
            if (CROSS_STREET_KEY.startsWith(left)) {
              block = block.substring(0,pt).trim();
            }
          }
        }
      }
      
      // If copy of address, skip it
      if (data.strAddress.startsWith(block)) return;
      if (block.startsWith(data.strAddress)) block = block.substring(data.strAddress.length()).trim();
      
      // Strip off leading colon
      block = stripFieldStart(block, ":");
      
      // append whatever is left to the info
      data.strSupp = append(data.strSupp, "\n", block);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME X UNIT INFO";
    }
  }
}
