package net.anei.cadpage.parsers.OH;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWarrenCountyCParser extends FieldProgramParser {

  public OHWarrenCountyCParser() {
    super("WARREN COUNTY", "OH", 
          "CALL! Loc:PLACE? Add:ADDR! Bld:APT? Flag:FLAG! Comp:NAME! Px:PHONE! Map:MAP! Beat:MAP! Badge:SKIP? Off:SKIP? XSt1:X? XSt2:X? Cmt:COMMENT");
  }
  
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  public String adjustMapAddress(String addr) {
    addr = STAB_PTN.matcher(addr).replaceAll("ST");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern STAB_PTN = Pattern.compile("\\bST[AB]\\b", Pattern.CASE_INSENSITIVE);
  
  private static Pattern CALLID = Pattern.compile("([A-Z][A-Z0-9]{0,2}\\d{12,14}) +(.*)");
  protected boolean parseMsg(String subject, String body, Data data) {
    //parse call id from head of string
    Matcher mat = CALLID.matcher(body);
    if (!mat.matches()) return false;
    data.strCallId = mat.group(1);
    return super.parseMsg(mat.group(2), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("FLAG")) return new MyFlagField();
    if (name.equals("COMMENT")) return new MyCommentField();
    return super.getField(name);
  }
  
  public class MyFlagField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (!field.equals("..N/A..") && !field.equals("----")) super.parse(field, data);
    }
  }
  
  private static Pattern DATETIME = Pattern.compile("(\\d{2}/\\d{2}/\\d{2}) (\\d{2}:\\d{2})\\b(.*)");
  private static Pattern TIME = Pattern.compile("(\\d{2}:\\d{2})\\b(.*)");
  public class MyCommentField extends Field {
    @Override
    public void parse(String field, Data data) {
      //get indices of arbitrarily ordered tags
      int[] indices = new int[]{
          field.indexOf("Incident Initiated By: "),
          field.indexOf("Original Location : "),
          field.indexOf("Primary Event: "),
          field.indexOf("Opened: "),
          field.indexOf("Time: ")
      };
      int[] tagLengths = new int[]{23,20,15,8,6};
      int fieldLength = field.length();
      
      // get ends of fields based on index of next field
      int[] ends = new int[indices.length];
      for (int e = 0; e < indices.length; e++) {
        // if field doesn't exist, neither does end
        if (indices[e] == -1) {
          ends[e] = -1;
          continue;
        }
        // else find closest higher index
        ends[e] = fieldLength;
        for (int i = 0; i < indices.length; i++)
          if (indices[i] > indices[e] && indices[i] < ends[e]) ends[e] = indices[i];
      }
      
      // parse tags to fields
      String[] fields = new String[indices.length];
      Arrays.fill(fields, "");
      for (int i = 0; i < indices.length; i++) {
        if (indices[i] != -1) fields[i] = field.substring(indices[i], ends[i]).substring(tagLengths[i]);
      }
      
      // INFO
      data.strSupp = append(data.strSupp, " / ", append(fields[0].trim(), " / ", fields[1].trim()));
      // DATETIME
      if (indices[3] != -1) {
        Matcher dtMat = DATETIME.matcher(fields[3]);
        if (dtMat.matches()) {
          data.strDate = dtMat.group(1);
          data.strTime = dtMat.group(2);
        }
      } 
      // TIME
      if (data.strTime.length() == 0) {
        Matcher tMat = TIME.matcher(fields[4]);
        if (tMat.matches()) {
          data.strTime = tMat.group(1);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO DATE TIME";
    }
  }
  
}
