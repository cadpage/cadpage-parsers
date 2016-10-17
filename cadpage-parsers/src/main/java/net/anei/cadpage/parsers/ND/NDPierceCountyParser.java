package net.anei.cadpage.parsers.ND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NDPierceCountyParser extends FieldProgramParser {

  public NDPierceCountyParser() {
    super("PIERCE COUNTY", "ND", "CALL CITY! ADDR");
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " INFO DATE TIME";
  }
  
  private static Pattern DATETIME = Pattern.compile("(.*) \\(Reported on (\\d{1,2}/\\d{1,2}/\\d{4}) @ (\\d{1,2}:\\d{2}:\\d{2} [AP]M)\\)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("attn:") && !subject.equals("Attention")) return false;
    
    //parse trailing DATETIME from body
    Matcher mat = DATETIME.matcher(body);
    String date = "", time = "";
    if (mat.matches()) {
      body = mat.group(1).trim();
      date = mat.group(2);
      time = mat.group(3);
    }
    
    //parse "Message Only" format as General Alert
    if (body.startsWith("Message Only: ")) data.parseGeneralAlert(this, body.substring(14, body.length()));
    else {
      //split by first ", " to isolate trailing INFO+
      String head = body;
      int i = body.indexOf(", ");
      if (i != -1) {
        head = body.substring(0, i).trim();
        data.strSupp = body.substring(i+2, body.length()).trim();
      } //try parsing normally, if that fails parse body (not head) as general alert
      String[] fields = head.split(",");
      if (parseFields(fields, data)) {
        
        // If we did not find an address, see if we can find one in the info
        if (data.strAddress.length() == 0) {
          Parser p = new Parser(data.strSupp);
          String addr = p.get(',');
          if (isValidAddress(addr)) {
            parseAddress(addr, data);
            data.strSupp = p.get();
          }
        }
      } else {
        data.parseGeneralAlert(this, body);
      }
    }
    
    //set date and time now that parser wont be reinitialized
    data.strDate = date;
    data.strTime = time;
    
    //out
    return true;
  }
  
}
