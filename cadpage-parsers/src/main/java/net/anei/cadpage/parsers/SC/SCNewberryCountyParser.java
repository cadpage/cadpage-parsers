package net.anei.cadpage.parsers.SC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCNewberryCountyParser extends FieldProgramParser {

  public SCNewberryCountyParser() {
    super("NEWBERRY COUNTY", "SC", 
          "DATETIME ID? CALL CODE ADDR! INFO+");
    setupMultiWordStreets("C AND D");
    setupProtectedNames("C AND D");
  }
  
  @Override
  public String getFilter() {
    return "9 11,911@ncso.sc.gov";
  }
  
  private static final Pattern DELIM = Pattern.compile("\\n{1,2}");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD CALL")) {
      subject = subject.toUpperCase();
      if (subject.startsWith("STATION ")) {
        subject = subject.substring(8).trim();
        subject = subject.replace(" AND ", " ");
        for (String src : subject.split(" +")) {
          data.strSource = append(data.strSource, " ", "ST" + src);
        }
      } else {
        data.strSource = subject.replaceAll("ST +", "ST");
      }
    }
    return parseFields(DELIM.split(body), data);
  }
  
  @Override public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ID")) return new IdField("CAD No - (.*)", true);
    if (name.equals("CODE")) return new MyCodeField("Event Code - (.*)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new CallField("(?:Description - )?(.*)", true);
    return super.getField(name);
  }
  
  private static Pattern DATE_TIME_PTN = Pattern.compile("Date/Time Sent - (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d [AP]M)");
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = DATE_TIME_PTN.matcher(field);
      if (!mat.matches()) abort();
      data.strDate = mat.group(1);
      setTime(TIME_FMT, mat.group(2), data);
    }
  }

  // crossroad follows address
  private static Pattern ADDR_CROSS = Pattern.compile("Location - *(.*)");
  private static Pattern CROSS = Pattern.compile(" cross ", Pattern.CASE_INSENSITIVE);
  private static Pattern APT_PTN = Pattern.compile("([A-Z]?\\d+[A-Z]?)|(?:APT|ROOM|RM|LOT) +([^ ]+) *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = ADDR_CROSS.matcher(field);
      if (!mat.matches()) abort();
      field = mat.group(1).trim();
      field = field.replace("C&D", "C AND D");
      String[] fields = CROSS.split(field);
      String addr = fields[0].trim();
      int pt = addr.indexOf("  ");
      if (pt >= 0) {
        String info = addr.substring(pt+2).trim();
        addr = addr.substring(0,pt);
        mat = APT_PTN.matcher(info);
        if (mat.matches()) {
          String apt = mat.group(1);
          if (apt != null) {
            info = "";
          } else {
            apt = mat.group(2);
            info = mat.group(3);
          }
          data.strApt = apt;
        }
        data.strSupp = append(data.strSupp, " / ", info);
      }
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
      for (int i = 1; i < fields.length; i++) data.strCross = append(data.strCross, " / ", fields[i].trim());
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " INFO X";
    }
  }
  
  //theres an instance of "Event Code - 1025  TOOK MEDICATION" that this class takes care of
  private static Pattern CODE_INFO = Pattern.compile("(\\d{4}|SIG\\d+) *(.*)");
  private class MyCodeField extends CodeField {
    // extended constructor to keep validation patterns in one place
    public MyCodeField(String vPattern, boolean hard) {
      super(vPattern, hard);
    }

    @Override
    public void parse(String field, Data data) {
      Matcher mat = CODE_INFO.matcher(field);
      if (!mat.matches()) abort();
      super.parse(mat.group(1), data);
      data.strCall = append(data.strCall, " / ", mat.group(2));
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

}
