package net.anei.cadpage.parsers.dispatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA15Parser extends FieldProgramParser {
  
  private static final Pattern NUMBER_PTN = Pattern.compile(" \\[(\\d{2})\\](?: |$)");
  private static final Pattern TRAIL_MARKER = Pattern.compile(" #\\[\\d"); 
  
  public DispatchA15Parser(String defCity, String defState) {
    super(defCity, defState,
           "RT:CALL! Loc:ADDR!");
  }
  
  private static class NumberedLine implements Comparable<NumberedLine> {
    int number;
    String line;
    
    public NumberedLine(int number, String line) {
      this.number = number;
      this.line = line;
    }
    
    
    @Override
    public int compareTo(NumberedLine another) {
      return number-another.number;
    }
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // They have an odd message numbering system, which wouldn't be a problem
    // except that mesasges get split up and come in in different orders, and
    // the bracket numbering system is the only way we can put them back together
    Matcher match = NUMBER_PTN.matcher(body);
    int lpt = 0;
    if (match.find()) {
      List<NumberedLine> list = new ArrayList<NumberedLine>();
      do {
        list.add(new NumberedLine(Integer.parseInt(match.group(1)), body.substring(lpt, match.start())));
        lpt = match.end();
      } while (match.find());
      if (lpt < body.length()) {
        data.expectMore = true;
        list.add(new NumberedLine(9999, body.substring(lpt)));
      }
      
      // If only one line was found, we can save us a lot of trouble
      if (list.size() == 1) {
        body = list.get(0).line.trim();
      }
      
      else {
        
        // Check for wraparound at 100
        boolean wrap = false;
        for (NumberedLine line : list) {
          if (line.number >= 90) {
            wrap = true;
            break;
          }
        }
        if (wrap) {
          for (NumberedLine line : list) {
            if (line.number <= 10) line.number += 100;
          }
        }
        
        // sort list by number and rebuild body
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (NumberedLine line : list) sb.append(line.line);
        body = sb.toString().trim();
      }
    }
    
    // There is always some trailing junk that needs to be stripped off.
    // But if we don't find it, assume more is coming
    
    else {
      
      match = TRAIL_MARKER.matcher(body);
      if (match.find()) {
        body = body.substring(0,match.start()).trim();
      } else {
        data.expectMore = true;
      }

      if (body.endsWith("#")) body = body.substring(0,body.length()-1).trim();
    }

    return super.parseMsg(body, data);
  }
  
  private static final Pattern MISMATCHED_PAREN_PTN = Pattern.compile("\\([^\\)]*$");
  private static final Pattern INTERSECTION_PTN = Pattern.compile("([^,]*),([^/]*)/([^,]*),[^,]*");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      
      // If it looks like we are missing a closing right paren, add it and set the
      // expect more data flag
      if (MISMATCHED_PAREN_PTN.matcher(field).find()) {
        data.expectMore = true;
        field = field + ')';
      }
      
      // Split off trailing term in parenthesis
      String extra = "";
      if (field.endsWith(")")) {
        int pt = field.lastIndexOf('(');
        extra = field.substring(pt+1,field.length()-1).trim();
        field = field.substring(0,pt).trim();
        
        // Trailing term might have a Near: qualifier
        pt = extra.indexOf("; Near:");
        if (pt >= 0) {
          data.strSupp = extra.substring(pt+2);
          extra =  extra.substring(0,pt).trim();
        }
      }
      
      // No trailing paren term?
      // Look for an street, city/street, city pattern
      else {
        Matcher match = INTERSECTION_PTN.matcher(field);
        if (match.matches()) {
          parseAddress(match.group(1).trim() + " & " + match.group(3).trim(), data);
          data.strCity = match.group(2).trim();
          return;
        }
      }
      
      // Rules all change when we start with an @
      if (field.startsWith("@")) {
        parseAddress(StartType.START_PLACE, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, field.substring(1).trim(), data);
        if (extra.length() > 0) {
          int pt = extra.lastIndexOf(',');
          if (pt > 0) {
            data.strCity = extra.substring(pt+1).trim();
            extra = extra.substring(0,pt).trim();
          }
          if (data.strAddress.length() == 0) {
            parseAddress(extra, data);
            extra = "";
          } else if (data.strAddress.equals(extra)) {
            extra = "";
          }
        }
      } 
      
      // Regular non-@ rules just address comma city
      else {
        int pt = field.lastIndexOf(',');
        if (pt > 0) {
          data.strCity = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim();
        }
        parseAddress(field, data);
      }
      
      // At this point whatever is left in extra is a cross street
      if (extra.startsWith("/") || extra.startsWith(",")) extra = extra.substring(1).trim();
      if (extra.endsWith("/") || extra.endsWith(",")) extra = extra.substring(0,extra.length()-1).trim();
      data.strCross = extra;
      return;
      
      // Trim leading and trailing slash or comma from cross street
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY X INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
}
