package net.anei.cadpage.parsers.SC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
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
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
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
  
  private static final Pattern I_26_PTN = Pattern.compile("\\bI26\\b");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return I_26_PTN.matcher(address).replaceAll("I 26");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "61 E I 26",                            "+34.431999,-81.741685",
      "62 E I 26",                            "+34.426928,-81.726269",
      "63 E I 26",                            "+34.417468,-81.711992",
      "64 E I 26",                            "+34.407038,-81.699684",
      "65 E I 26",                            "+34.394638,-81.691010",
      "66 E I 26",                            "+34.381795,-81.682917",
      "67 E I 26",                            "+34.370441,-81.671857",
      "68 E I 26",                            "+34.359955,-81.659730",
      "69 E I 26",                            "+34.352095,-81.646137",
      "70 E I 26",                            "+34.343692,-81.631485",
      "71 E I 26",                            "+34.335440,-81.617052",
      "72 E I 26",                            "+34.327226,-81.602716",
      "73 E I 26",                            "+34.318689,-81.587967",
      "74 E I 26",                            "+34.310443,-81.573570",
      "75 E I 26",                            "+34.301948,-81.558821",
      "76 E I 26",                            "+34.292625,-81.545285",
      "77 E I 26",                            "+34.284662,-81.532204",
      "78 E I 26",                            "+34.276222,-81.518051",
      "79 E I 26",                            "+34.265764,-81.505706",
      "80 E I 26",                            "+34.255967,-81.492874",
      "81 E I 26",                            "+34.247360,-81.479298",
      "82 E I 26",                            "+34.239872,-81.463989",
      "83 E I 26",                            "+34.234629,-81.447803",
      "84 E I 26",                            "+34.227939,-81.432329",
      "85 E I 26",                            "+34.222136,-81.416508",
      "86 E I 26",                            "+34.216237,-81.399916",
      "87 E I 26",                            "+34.210071,-81.384778",
      "88 E I 26",                            "+34.203227,-81.369146",
      "89 E I 26",                            "+34.195220,-81.354608",
      "90 E I 26",                            "+34.187326,-81.340091",
      "61 W I 26",                            "+34.432235,-81.741567",
      "62 W I 26",                            "+34.427177,-81.726154",
      "63 W I 26",                            "+34.417800,-81.711628",
      "64 W I 26",                            "+34.407351,-81.699299",
      "65 W I 26",                            "+34.394825,-81.690535",
      "66 W I 26",                            "+34.381982,-81.682632",
      "67 W I 26",                            "+34.370583,-81.671594",
      "68 W I 26",                            "+34.360175,-81.659558",
      "69 W I 26",                            "+34.352310,-81.645931",
      "70 W I 26",                            "+34.343918,-81.631354",
      "71 W I 26",                            "+34.335642,-81.616929",
      "72 W I 26",                            "+34.327462,-81.602593",
      "73 W I 26",                            "+34.318980,-81.587786",
      "74 W I 26",                            "+34.310691,-81.573373",
      "75 W I 26",                            "+34.302167,-81.558600",
      "76 W I 26",                            "+34.292818,-81.545066",
      "77 W I 26",                            "+34.284888,-81.531991",
      "78 W I 26",                            "+34.276436,-81.517798",
      "79 W I 26",                            "+34.265926,-81.505453",
      "80 W I 26",                            "+34.256180,-81.492656",
      "81 W I 26",                            "+34.247567,-81.479063",
      "82 W I 26",                            "+34.240174,-81.463835",
      "83 W I 26",                            "+34.234855,-81.447658",
      "84 W I 26",                            "+34.228167,-81.432174",
      "85 W I 26",                            "+34.222379,-81.416382",
      "86 W I 26",                            "+34.216523,-81.399793",
      "87 W I 26",                            "+34.210373,-81.384644",
      "88 W I 26",                            "+34.203491,-81.369016",
      "89 W I 26",                            "+34.195468,-81.354463",
      "90 W I 26",                            "+34.187524,-81.339964"
  });
}
