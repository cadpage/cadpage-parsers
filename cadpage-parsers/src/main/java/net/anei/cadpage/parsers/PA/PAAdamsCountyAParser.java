package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

/**
 * Adams County, PA
 */
public class PAAdamsCountyAParser extends DispatchA1Parser {
  
  public PAAdamsCountyAParser() {
    super(CITY_LIST, "ADAMS COUNTY", "PA");
    for (String city : CITY_LIST) {
      if (city.endsWith(" BORO")) setupCities(city.substring(0,city.length()-5));
    }
    setupCities(MD_CITIES);
    setupCities(MISTYPED_CITIES);
    addExtendedDirections();
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "adams911@adamscounty.us,messaging@iamresponding.com,777";
  }
  
  private static final Pattern IAMR_PREFIX1 = Pattern.compile("^(?:Alert: +)?(.*?)[ \n](?=ALRM LVL:|: +BOX )");
  private static final Pattern IAMR_BOX_PTN = Pattern.compile("[, ] +BOX ");
  private static final Pattern IAMR_COMMA_PTN = Pattern.compile("[ ,]*\n[ ,]*");
  private static final Pattern TOWNSHIP_PTN = Pattern.compile("\\bTOWNSHIP\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Check for garbled prefix associated with IamResponding
    Matcher match = IAMR_PREFIX1.matcher(body);
    if (match.lookingAt()) {
      data.strSource = subject;
      subject = "Alert: " + match.group(1).trim();
      body = body.substring(match.end()).trim();
      if (body.startsWith(":")) {
        body = "RUN CARD:" + body.substring(1);
      } else {
        body = IAMR_BOX_PTN.matcher(body).replaceFirst(", RUN CARD: BOX ");
      }
      body = IAMR_COMMA_PTN.matcher(body).replaceAll("\n");
      body = body.replaceAll(" , ", " ");
    }
    
    body = TOWNSHIP_PTN.matcher(body).replaceAll("TWP");
    if (!super.parseMsg(subject, body, data)) return false;
    
    // See if a doubled city name has been interpretted as an apt
    data.strApt = stripFieldStart(data.strApt, "TRL ");
    String apt = data.strApt;
    if (data.strCity.length() == 0) {
      int pt = apt.indexOf(" - ");
      if (pt >= 0) {
        String part1 = apt.substring(0,pt).trim();
        String part2 = apt.substring(pt+2).trim();
        if (part1.endsWith("COUNTY")) part1 = part2;
        if (isCity(part1)) {
          data.strCity = part1;
          data.strApt = "";
        }
      }
    }
    
    String city = data.strCity.toUpperCase();
    city = stripFieldEnd(city, " BORO");
    city = convertCodes(city, MISTYPED_CITIES);
    if (city.endsWith(" CO")) city += "UNTY";
    data.strCity = city;
    if (MD_CITIES.contains(city)) data.strState = "MD";
    
    data.strSupp = data.strSupp.replace(" / ", "\n");
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram().replace("CITY", "CITY ST");
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_RECHECK_APT;
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    address = address.toUpperCase();
    if (address.equals("90 KNIGHT RD")) address = append(address, " LOT ", apt);
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "0 US 15 NB",                           "+39.720100,-77.306500",
      "0.2 US 15 NB",                         "+39.722600,-77.305100",
      "0.4 US 15 NB",                         "+39.725300,-77.303700",
      "0.6 US 15 NB",                         "+39.727900,-77.302300",
      "0.8 US 15 NB",                         "+39.730700,-77.300800",
      "1 US 15 NB",                           "+39.733400,-77.299300",
      "1.2 US 15 NB",                         "+39.735800,-77.297000",
      "1.4 US 15 NB",                         "+39.737300,-77.294000",
      "1.6 US 15 NB",                         "+39.738600,-77.290700",
      "1.8 US 15 NB",                         "+39.740000,-77.287400",
      "2 US 15 NB",                           "+39.741300,-77.284100",
      "2.2 US 15 NB",                         "+39.742700,-77.280700",
      "2.4 US 15 NB",                         "+39.744100,-77.277500",
      "2.6 US 15 NB",                         "+39.746100,-77.274700",
      "2.8 US 15 NB",                         "+39.748600,-77.272800",
      "3 US 15 NB",                           "+39.751100,-77.270900",
      "3.2 US 15 NB",                         "+39.753600,-77.269100",
      "3.4 US 15 NB",                         "+39.756100,-77.267200",
      "3.6 US 15 NB",                         "+39.758700,-77.265300",
      "3.8 US 15 NB",                         "+39.761200,-77.263500",
      "4 US 15 NB",                           "+39.763400,-77.261100",
      "4.2 US 15 NB",                         "+39.765000,-77.258200",
      "4.4 US 15 NB",                         "+39.766300,-77.254800",
      "4.6 US 15 NB",                         "+39.767600,-77.251500",
      "4.8 US 15 NB",                         "+39.768800,-77.248100",
      "5 US 15 NB",                           "+39.770100,-77.244700",
      "5.2 US 15 NB",                         "+39.771400,-77.241300",
      "5.4 US 15 NB",                         "+39.772700,-77.237900",
      "5.6 US 15 NB",                         "+39.773900,-77.234600",
      "5.8 US 15 NB",                         "+39.775500,-77.231300",
      "6 US 15 NB",                           "+39.777200,-77.228300",
      "6.2 US 15 NB",                         "+39.779100,-77.225500",
      "6.4 US 15 NB",                         "+39.781200,-77.222900",
      "6.6 US 15 NB",                         "+39.783300,-77.220400",
      "6.8 US 15 NB",                         "+39.785500,-77.217900",
      "7 US 15 NB",                           "+39.787600,-77.215300",
      "7.2 US 15 NB",                         "+39.789500,-77.212400",
      "7.4 US 15 NB",                         "+39.791200,-77.209400",
      "7.6 US 15 NB",                         "+39.793000,-77.206400",
      "7.8 US 15 NB",                         "+39.795100,-77.203800",
      "8 US 15 NB",                           "+39.797600,-77.201800",
      "8.2 US 15 NB",                         "+39.800100,-77.200000",
      "8.4 US 15 NB",                         "+39.802600,-77.198200",
      "8.6 US 15 NB",                         "+39.804700,-77.196800",
      "8.8 US 15 NB",                         "+39.807300,-77.195000",
      "9 US 15 NB",                           "+39.810100,-77.194100",
      "9.2 US 15 NB",                         "+39.813000,-77.194200",
      "9.4 US 15 NB",                         "+39.815800,-77.195300",
      "9.6 US 15 NB",                         "+39.818400,-77.196600",
      "9.8 US 15 NB",                         "+39.821200,-77.197800",
      "10 US 15 NB",                          "+39.824100,-77.198100",
      "10.2 US 15 NB",                        "+39.826700,-77.197500",
      "10.4 US 15 NB",                        "+39.829600,-77.196700",
      "10.6 US 15 NB",                        "+39.832500,-77.196000",
      "10.8 US 15 NB",                        "+39.835400,-77.195200",
      "11 US 15 NB",                          "+39.838200,-77.194500",
      "11.2 US 15 NB",                        "+39.841100,-77.194100",
      "11.4 US 15 NB",                        "+39.844000,-77.194500",
      "11.6 US 15 NB",                        "+39.846700,-77.195200",
      "11.8 US 15 NB",                        "+39.849600,-77.195900",
      "12 US 15 NB",                          "+39.852500,-77.196600",
      "12.2 US 15 NB",                        "+39.855400,-77.197400",
      "12.4 US 15 NB",                        "+39.858300,-77.197900",
      "12.6 US 15 NB",                        "+39.861200,-77.197700",
      "12.8 US 15 NB",                        "+39.864000,-77.196900",
      "13 US 15 NB",                          "+39.866200,-77.195700",
      "13.2 US 15 NB",                        "+39.868900,-77.194200",
      "13.4 US 15 NB",                        "+39.871500,-77.192600",
      "13.6 US 15 NB",                        "+39.874100,-77.191100",
      "13.8 US 15 NB",                        "+39.876800,-77.189500",
      "14 US 15 NB",                          "+39.879400,-77.188000",
      "14.2 US 15 NB",                        "+39.882000,-77.186500",
      "14.4 US 15 NB",                        "+39.884700,-77.184900",
      "14.6 US 15 NB",                        "+39.887300,-77.183300",
      "14.8 US 15 NB",                        "+39.889800,-77.181300",
      "15 US 15 NB",                          "+39.892200,-77.179200",
      "15.2 US 15 NB",                        "+39.894700,-77.177100",
      "15.4 US 15 NB",                        "+39.897100,-77.175100",
      "15.6 US 15 NB",                        "+39.899500,-77.173000",
      "15.8 US 15 NB",                        "+39.901900,-77.171000",
      "16 US 15 NB",                          "+39.904300,-77.169000",
      "16.2 US 15 NB",                        "+39.906900,-77.167300",
      "16.4 US 15 NB",                        "+39.909600,-77.166100",
      "16.6 US 15 NB",                        "+39.912400,-77.165000",
      "16.8 US 15 NB",                        "+39.915100,-77.163800",
      "17 US 15 NB",                          "+39.917900,-77.162700",
      "17.2 US 15 NB",                        "+39.920800,-77.161500",
      "17.4 US 15 NB",                        "+39.923500,-77.160300",
      "17.6 US 15 NB",                        "+39.926100,-77.158700",
      "17.8 US 15 NB",                        "+39.928500,-77.156500",
      "18 US 15 NB",                          "+39.930600,-77.153900",
      "18.2 US 15 NB",                        "+39.932600,-77.151200",
      "18.4 US 15 NB",                        "+39.934500,-77.148600",
      "18.6 US 15 NB",                        "+39.936600,-77.145900",
      "18.8 US 15 NB",                        "+39.938600,-77.143200",
      "19 US 15 NB",                          "+39.940700,-77.140400",
      "19.2 US 15 NB",                        "+39.942700,-77.137800",
      "19.4 US 15 NB",                        "+39.945300,-77.136200",
      "19.6 US 15 NB",                        "+39.948100,-77.135200",
      "19.8 US 15 NB",                        "+39.950900,-77.134200",
      "20 US 15 NB",                          "+39.953700,-77.133100",
      "20.2 US 15 NB",                        "+39.956500,-77.132100",
      "20.4 US 15 NB",                        "+39.959300,-77.131100",
      "20.6 US 15 NB",                        "+39.962100,-77.130100",
      "20.8 US 15 NB",                        "+39.964900,-77.129200",
      "21 US 15 NB",                          "+39.967800,-77.128600",
      "21.2 US 15 NB",                        "+39.970600,-77.128100",
      "21.4 US 15 NB",                        "+39.973500,-77.127500",
      "21.6 US 15 NB",                        "+39.976300,-77.127000",
      "21.8 US 15 NB",                        "+39.979000,-77.125700",
      "22 US 15 NB",                          "+39.981600,-77.124000",
      "22.2 US 15 NB",                        "+39.984200,-77.122400",
      "22.4 US 15 NB",                        "+39.986800,-77.120800",
      "22.6 US 15 NB",                        "+39.989500,-77.119100",
      "22.8 US 15 NB",                        "+39.991600,-77.116700",
      "23 US 15 NB",                          "+39.993800,-77.114100",
      "23.2 US 15 NB",                        "+39.995900,-77.111700",
      "23.4 US 15 NB",                        "+39.998000,-77.109200",
      "23.6 US 15 NB",                        "+40.000200,-77.106700",
      "23.8 US 15 NB",                        "+40.002400,-77.104300",
      "24 US 15 NB",                          "+40.005000,-77.102800",
      "24.2 US 15 NB",                        "+40.007900,-77.102100",
      "24.4 US 15 NB",                        "+40.010800,-77.101400",
      "24.6 US 15 NB",                        "+40.013400,-77.099900",
      "24.8 US 15 NB",                        "+40.015900,-77.098100",
      "25 US 15 NB",                          "+40.018200,-77.095800",
      "25.2 US 15 NB",                        "+40.020500,-77.093400",
      "25.4 US 15 NB",                        "+40.022800,-77.091100",
      "25.6 US 15 NB",                        "+40.025000,-77.088800",
      "25.8 US 15 NB",                        "+40.027300,-77.086400",
      "26 US 15 NB",                          "+40.029500,-77.084100",
      "26.2 US 15 NB",                        "+40.031800,-77.081800",
      "26.4 US 15 NB",                        "+40.034200,-77.079600",
      "26.6 US 15 NB",                        "+40.036800,-77.077700",
      "0 US 15 SB",                           "+39.720100,-77.306800",
      "0.2 US 15 SB",                         "+39.722600,-77.305500",
      "0.4 US 15 SB",                         "+39.725200,-77.304100",
      "0.6 US 15 SB",                         "+39.727900,-77.302600",
      "0.8 US 15 SB",                         "+39.730600,-77.301200",
      "1 US 15 SB",                           "+39.733300,-77.299700",
      "1.2 US 15 SB",                         "+39.735900,-77.297300",
      "1.4 US 15 SB",                         "+39.737400,-77.294300",
      "1.6 US 15 SB",                         "+39.738800,-77.291000",
      "1.8 US 15 SB",                         "+39.740200,-77.287600",
      "2 US 15 SB",                           "+39.741500,-77.284300",
      "2.2 US 15 SB",                         "+39.742900,-77.281000",
      "2.4 US 15 SB",                         "+39.744200,-77.277700",
      "2.6 US 15 SB",                         "+39.746100,-77.275100",
      "2.8 US 15 SB",                         "+39.748500,-77.273200",
      "3 US 15 SB",                           "+39.751000,-77.271400",
      "3.2 US 15 SB",                         "+39.753500,-77.269500",
      "3.4 US 15 SB",                         "+39.756100,-77.267600",
      "3.6 US 15 SB",                         "+39.758600,-77.265700",
      "3.8 US 15 SB",                         "+39.761200,-77.263800",
      "4 US 15 SB",                           "+39.763700,-77.261500",
      "4.2 US 15 SB",                         "+39.765500,-77.258700",
      "4.4 US 15 SB",                         "+39.766800,-77.255300",
      "4.6 US 15 SB",                         "+39.768000,-77.251900",
      "4.8 US 15 SB",                         "+39.769200,-77.248500",
      "5 US 15 SB",                           "+39.770400,-77.245100",
      "5.2 US 15 SB",                         "+39.771600,-77.241700",
      "5.4 US 15 SB",                         "+39.772800,-77.238300",
      "5.6 US 15 SB",                         "+39.774100,-77.234900",
      "5.8 US 15 SB",                         "+39.775500,-77.231700",
      "6 US 15 SB",                           "+39.777200,-77.228700",
      "6.2 US 15 SB",                         "+39.779200,-77.225900",
      "6.4 US 15 SB",                         "+39.781200,-77.223300",
      "6.6 US 15 SB",                         "+39.783500,-77.220900",
      "6.8 US 15 SB",                         "+39.785700,-77.218400",
      "7 US 15 SB",                           "+39.787800,-77.215800",
      "7.2 US 15 SB",                         "+39.789800,-77.212900",
      "7.4 US 15 SB",                         "+39.791400,-77.209900",
      "7.6 US 15 SB",                         "+39.793100,-77.206900",
      "7.8 US 15 SB",                         "+39.795200,-77.204300",
      "8 US 15 SB",                           "+39.797600,-77.202200",
      "8.2 US 15 SB",                         "+39.800100,-77.200400",
      "8.4 US 15 SB",                         "+39.802600,-77.198600",
      "8.6 US 15 SB",                         "+39.804600,-77.197200",
      "8.8 US 15 SB",                         "+39.807200,-77.195400",
      "9 US 15 SB",                           "+39.809800,-77.194600",
      "9.2 US 15 SB",                         "+39.812700,-77.194600",
      "9.4 US 15 SB",                         "+39.815500,-77.195700",
      "9.6 US 15 SB",                         "+39.818200,-77.197000",
      "9.8 US 15 SB",                         "+39.821000,-77.198200",
      "10 US 15 SB",                          "+39.823900,-77.198500",
      "10.2 US 15 SB",                        "+39.826800,-77.197900",
      "10.4 US 15 SB",                        "+39.829700,-77.197200",
      "10.6 US 15 SB",                        "+39.832500,-77.196500",
      "10.8 US 15 SB",                        "+39.835300,-77.195800",
      "11 US 15 SB",                          "+39.838200,-77.195100",
      "11.2 US 15 SB",                        "+39.841000,-77.194600",
      "11.4 US 15 SB",                        "+39.843900,-77.194800",
      "11.6 US 15 SB",                        "+39.846600,-77.195500",
      "11.8 US 15 SB",                        "+39.849400,-77.196200",
      "12 US 15 SB",                          "+39.852400,-77.196900",
      "12.2 US 15 SB",                        "+39.855200,-77.197600",
      "12.4 US 15 SB",                        "+39.858200,-77.198200",
      "12.6 US 15 SB",                        "+39.861100,-77.198000",
      "12.8 US 15 SB",                        "+39.863900,-77.197300",
      "13 US 15 SB",                          "+39.866100,-77.196100",
      "13.2 US 15 SB",                        "+39.868800,-77.194600",
      "13.4 US 15 SB",                        "+39.871400,-77.193000",
      "13.6 US 15 SB",                        "+39.874100,-77.191500",
      "13.8 US 15 SB",                        "+39.876700,-77.189900",
      "14 US 15 SB",                          "+39.879300,-77.188400",
      "14.2 US 15 SB",                        "+39.882000,-77.186900",
      "14.4 US 15 SB",                        "+39.884600,-77.185300",
      "14.6 US 15 SB",                        "+39.887300,-77.183700",
      "14.8 US 15 SB",                        "+39.889700,-77.181700",
      "15 US 15 SB",                          "+39.892200,-77.179600",
      "15.2 US 15 SB",                        "+39.894600,-77.177600",
      "15.4 US 15 SB",                        "+39.897000,-77.175500",
      "15.6 US 15 SB",                        "+39.899500,-77.173500",
      "15.8 US 15 SB",                        "+39.901800,-77.171500",
      "16 US 15 SB",                          "+39.904300,-77.169400",
      "16.2 US 15 SB",                        "+39.906800,-77.167700",
      "16.4 US 15 SB",                        "+39.909500,-77.166500",
      "16.6 US 15 SB",                        "+39.912300,-77.165300",
      "16.8 US 15 SB",                        "+39.915000,-77.164200",
      "17 US 15 SB",                          "+39.917800,-77.163000",
      "17.2 US 15 SB",                        "+39.920600,-77.161900",
      "17.4 US 15 SB",                        "+39.923300,-77.160700",
      "17.6 US 15 SB",                        "+39.926100,-77.159200",
      "17.8 US 15 SB",                        "+39.928400,-77.157000",
      "18 US 15 SB",                          "+39.930600,-77.154300",
      "18.2 US 15 SB",                        "+39.932600,-77.151600",
      "18.4 US 15 SB",                        "+39.934600,-77.149000",
      "18.6 US 15 SB",                        "+39.936700,-77.146300",
      "18.8 US 15 SB",                        "+39.938700,-77.143600",
      "19 US 15 SB",                          "+39.940600,-77.141000",
      "19.2 US 15 SB",                        "+39.942700,-77.138300",
      "19.4 US 15 SB",                        "+39.945100,-77.136600",
      "19.6 US 15 SB",                        "+39.947900,-77.135600",
      "19.8 US 15 SB",                        "+39.950700,-77.134600",
      "20 US 15 SB",                          "+39.953500,-77.133500",
      "20.2 US 15 SB",                        "+39.956300,-77.132500",
      "20.4 US 15 SB",                        "+39.959100,-77.131500",
      "20.6 US 15 SB",                        "+39.961900,-77.130500",
      "20.8 US 15 SB",                        "+39.964600,-77.129600",
      "21 US 15 SB",                          "+39.967500,-77.129000",
      "21.2 US 15 SB",                        "+39.970400,-77.128500",
      "21.4 US 15 SB",                        "+39.973300,-77.127900",
      "21.6 US 15 SB",                        "+39.976200,-77.127300",
      "21.8 US 15 SB",                        "+39.978900,-77.126100",
      "22 US 15 SB",                          "+39.981500,-77.124500",
      "22.2 US 15 SB",                        "+39.984100,-77.122800",
      "22.4 US 15 SB",                        "+39.986700,-77.121200",
      "22.6 US 15 SB",                        "+39.989300,-77.119500",
      "22.8 US 15 SB",                        "+39.991500,-77.117000",
      "23 US 15 SB",                          "+39.993700,-77.114500",
      "23.2 US 15 SB",                        "+39.995800,-77.111900",
      "23.4 US 15 SB",                        "+39.998000,-77.109400",
      "23.6 US 15 SB",                        "+40.000200,-77.106900",
      "23.8 US 15 SB",                        "+40.002400,-77.104500",
      "24 US 15 SB",                          "+40.004900,-77.103000",
      "24.2 US 15 SB",                        "+40.007800,-77.102300",
      "24.4 US 15 SB",                        "+40.010700,-77.101600",
      "24.6 US 15 SB",                        "+40.013400,-77.100100",
      "24.8 US 15 SB",                        "+40.015900,-77.098300",
      "25 US 15 SB",                          "+40.018200,-77.096000",
      "25.2 US 15 SB",                        "+40.020400,-77.093700",
      "25.4 US 15 SB",                        "+40.022700,-77.091300",
      "25.6 US 15 SB",                        "+40.024900,-77.089000",
      "25.8 US 15 SB",                        "+40.027100,-77.086800",
      "26 US 15 SB",                          "+40.029400,-77.084400",
      "26.2 US 15 SB",                        "+40.031700,-77.082000",
      "26.4 US 15 SB",                        "+40.034100,-77.079900",
      "26.6 US 15 SB",                        "+40.036600,-77.078000",
      
      "90 KNIGHT RD LOT 1",  "39.77982295,-77.23336034",
      "90 KNIGHT RD LOT 2",  "39.77988196,-77.23356318",
      "90 KNIGHT RD LOT 3",  "39.77992756,-77.23380558",
      "90 KNIGHT RD LOT 4",  "39.77986907,-77.23399770",
      "90 KNIGHT RD LOT 5",  "39.77979229,-77.23418076",
      "90 KNIGHT RD LOT 6",  "39.77963022,-77.23434236",
      "90 KNIGHT RD LOT 7",  "39.77922337,-77.23458242",
      "90 KNIGHT RD LOT 8",  "39.77906001,-77.23449927",
      "90 KNIGHT RD LOT 9",  "39.77892963,-77.23438393",
      "90 KNIGHT RD LOT 10", "39.77880234,-77.23430581",
      "90 KNIGHT RD LOT 11", "39.77863795,-77.23415259",
      "90 KNIGHT RD LOT 12", "39.77945604,-77.23357525",
      "90 KNIGHT RD LOT 13", "39.77951582,-77.23380357",
      "90 KNIGHT RD LOT 14", "39.77926794,-77.23405436",
      "90 KNIGHT RD LOT 15", "39.77903630,-77.23389577",
      "90 KNIGHT RD LOT 16", "39.77918936,-77.23367181",
      "90 KNIGHT RD LOT 17", "39.77886727,-77.23377910",
      "90 KNIGHT RD LOT 18", "39.77880827,-77.23366678",
      "90 KNIGHT RD LOT 19", "39.77909505,-77.23322522",
      "90 KNIGHT RD LOT 20", "39.77892654,-77.23328792",
      "90 KNIGHT RD LOT 21", "39.77841430,-77.23312430",
      "90 KNIGHT RD LOT 22", "39.77823084,-77.23304585",
      "90 KNIGHT RD LOT 23", "39.77805923,-77.23298416",
      "90 KNIGHT RD LOT 24", "39.77794766,-77.23277628",
      "90 KNIGHT RD LOT 25", "39.77785052,-77.23262005",
      "90 KNIGHT RD LOT 26", "39.77849701,-77.23358732",
      "90 KNIGHT RD LOT 27", "39.77833803,-77.23356485",
      "90 KNIGHT RD LOT 28", "39.77819992,-77.23348740",
      "90 KNIGHT RD LOT 29", "39.77785361,-77.23345354",
      "90 KNIGHT RD LOT 30", "39.77780413,-77.23327082",
      "90 KNIGHT RD LOT 31", "39.77769901,-77.23310720",
      "90 KNIGHT RD LOT 32", "39.77759207,-77.23295096",
      "90 KNIGHT RD LOT 33", "39.77733801,-77.23260563",
      "90 KNIGHT RD LOT 34", "39.77721458,-77.23247755",
      "90 KNIGHT RD LOT 35", "39.77712079,-77.23232701",
      "90 KNIGHT RD LOT 36", "39.77948773,-77.23305356",
      "90 KNIGHT RD LOT 37", "39.77691259,-77.23201990",
      "90 KNIGHT RD LOT 38", "39.77670155,-77.23217178",
      "90 KNIGHT RD LOT 39", "39.77681339,-77.23232232",
      "90 KNIGHT RD LOT 40", "39.77693192,-77.23251645",
      "90 KNIGHT RD LOT 41", "39.77703782,-77.23266799",
      "90 KNIGHT RD LOT 42", "39.77711718,-77.23284435",
      "90 KNIGHT RD LOT 43", "39.77739238,-77.23326076",
      "90 KNIGHT RD LOT 44", "39.77749570,-77.23341063",
      "90 KNIGHT RD LOT 45", "39.77760702,-77.23361012",
      "90 KNIGHT RD LOT 46", "39.77774049,-77.23373450",
      "90 KNIGHT RD LOT 47", "39.77792395,-77.23385789",
      "90 KNIGHT RD LOT 48", "39.77812519,-77.23395377",
      "90 KNIGHT RD LOT 49", "39.77831071,-77.23400138",
      "90 KNIGHT RD LOT 50", "39.77787036,-77.23438662",
      "90 KNIGHT RD LOT 51", "39.77773972,-77.23425083",
      "90 KNIGHT RD LOT 52", "39.77762119,-77.23418813",
      "90 KNIGHT RD LOT 53", "39.77751529,-77.23406978",
      "90 KNIGHT RD LOT 54", "39.77738336,-77.23398127",
      "90 KNIGHT RD LOT 55", "39.77728287,-77.23377842",
      "90 KNIGHT RD LOT 56", "39.77720221,-77.23365203",
      "90 KNIGHT RD LOT 57", "39.77686904,-77.23315548",
      "90 KNIGHT RD LOT 58", "39.77670104,-77.23293420",
      "90 KNIGHT RD LOT 59", "39.77659075,-77.23276757",
      "90 KNIGHT RD LOT 60", "39.77643667,-77.23259926"
  });

  private static final String[] CITY_LIST = new String[]{
    
    // Boroughs
    "ABBOTTSTOWN BORO",
    "ARENDTSVILLE BORO",
    "BENDERSVILLE BORO",
    "BIGLERVILLE BORO",
    "BONNEAUVILLE BORO",
    "CARROLL VALLEY BORO",
    "EAST BERLIN BORO",
    "FAIRFIELD BORO",
    "GETTYSBURG BORO",
    "LITTLESTOWN BORO",
    "MCSHERRYSTOWN BORO",
    "NEW OXFORD BORO",
    "YORK SPRINGS BORO",

    // Townships
    "BERWICK TWP",
    "BUTLER TWP",
    "CONEWAGO TWP",
    "CUMBERLAND TWP",
    "FRANKLIN TWP",
    "FREEDOM TWP",
    "GERMANY TWP",
    "HAMILTON TWP",
    "HAMILTONBAN TWP",
    "HIGHLAND TWP",
    "HUNTINGTON TWP",
    "LATIMORE TWP",
    "LETTERKENNY TWP",
    "LIBERTY TWP",
    "MENALLEN TWP",
    "MOUNT JOY TWP",
    "MOUNT PLEASANT TWP",
    "OXFORD TWP",
    "READING TWP",
    "STRABAN TWP",
    "TYRONE TWP",
    "UNION TWP",

    // Census-designated places
    "ASPERS",
    "CASHTOWN",
    "FLORADALE",
    "GARDNERS",
    "HAMPTON",
    "HEIDLERSBURG",
    "HUNTERSTOWN",
    "IDAVILLE",
    "LAKE HERITAGE",
    "LAKE MEADE",
    "MIDWAY",
    "MCKNIGHTSTOWN",
    "ORRTANNA",
    "TABLE ROCK",
    
    // Cumberland County
    "CUMBERLAND COUNTY",
    "CUMBERLAND CO",
    
    // Boroughs
    "CAMP HILL BORO",
    "CARLISLE BORO",
    "LEMOYNE BORO",
    "MECHANICSBURG BORO",
    "MOUNT HOLLY SPRINGS BORO",
    "MT HOLLY SPRINGS BORO",
    "NEW CUMBERLAND BORO",
    "NEWBURG BORO",
    "NEWVILLE BORO",
    "SHIREMANSTOWN BORO",
    "WORMLEYSBURG BORO",

    // Townships
    "COOKE TWP",
    "DICKINSON TWP",
    "EAST PENNSBORO TWP",
    "HAMPDEN TWP",
    "HOPEWELL TWP",
    "LOWER ALLEN TWP",
    "LOWER FRANKFORD TWP",
    "LOWER MIFFLIN TWP",
    "MIDDLESEX TWP",
    "MONROE TWP",
    "NORTH MIDDLETON TWP",
    "NORTH NEWTON TWP",
    "PENN TWP",
    "SHIPPENSBURG TWP",
    "SILVER SPRING TWP",
    "SOUTH MIDDLETON TWP",
    "SOUTH NEWTON TWP",
    "SOUTHAMPTON TWP",
    "UPPER ALLEN TWP",
    "UPPER FRANKFORD TWP",
    "UPPER MIFFLIN TWP",
    "WEST PENNSBORO TWP",

    // Census-designated places
    "BOILING SPRINGS",
    "ENOLA",
    "LOWER ALLEN",
    "MESSIAH COLLEGE",
    "NEW KINGSTOWN",
    "PLAINFIELD",
    "SCHLUSSER",
    "WEST FAIRVIEW",

    // Unincorporated communities
    "BLOSERVILLE",
    "GRANTHAM",
    "SUMMERDALE",
    "LISBURN",
    
    // Franklin County
    "FRANKLIN COUNTY",
    "FRANKLIN CO",
    
    // Boroughs
    "CHAMBERSBURG BORO",
    "GREENCASTLE BORO",
    "MERCERSBURG BORO",
    "MONT ALTO BORO",
    "ORRSTOWN BORO",
    "SHIPPENSBURG BORO",
    "WAYNESBORO BORO",

    // Townships
    "ANTRIM TWP",
    "FANNETT TWP",
    "GREENE TWP",
    "GUILFORD TWP",
    "HAMILTON TWP",
    "LETTERKENNY TWP",
    "LURGAN TWP",
    "METAL TWP",
    "MONTGOMERY TWP",
    "PETERS TWP",
    "QUINCY TWP",
    "SOUTHAMPTON TWP",
    "ST. THOMAS TWP",
    "WARREN TWP",
    "WASHINGTON TWP",

    // Census-designated places
    "BLUE RIDGE SUMMIT",
    "FAYETTEVILLE",
    "FORT LOUDON",
    "GUILFORD",
    "MARION",
    "PEN MAR",
    "ROUZERVILLE",
    "SCOTLAND",
    "STATE LINE",
    "WAYNE HEIGHTS",
    
    // York County
    "YORK COUNTY",
    "YORK CO",
    
    // City
    "YORK",

    // Boroughs
    "CROSS ROADS BORO",
    "DALLASTOWN BORO",
    "DELTA BORO",
    "DILLSBURG BORO",
    "DOVER BORO",
    "EAST PROSPECT BORO",
    "FAWN GROVE BORO",
    "FELTON BORO",
    "FRANKLINTOWN BORO",
    "FRANKLINTOWN",
    "GLEN ROCK BORO",
    "GOLDSBORO BORO",
    "HALLAM BORO",
    "HANOVER BORO",
    "JACOBUS BORO",
    "JEFFERSON BORO",
    "LEWISBERRY BORO",
    "LOGANVILLE BORO",
    "MANCHESTER BORO",
    "MOUNT WOLF BORO",
    "NEW FREEDOM BORO",
    "NEW SALEM BORO",
    "NORTH YORK BORO",
    "RAILROAD BORO",
    "RED LION BORO",
    "SEVEN VALLEYS BORO",
    "SHREWSBURY BORO",
    "SPRING GROVE BORO",
    "STEWARTSTOWN BORO",
    "WELLSVILLE BORO",
    "WEST YORK BORO",
    "WINDSOR BORO",
    "WINTERSTOWN BORO",
    "WRIGHTSVILLE BORO",
    "YOE BORO",
    "YORK HAVEN BORO",
    "YORKANA BORO",

    // Townships
    "CARROLL TWP",
    "CHANCEFORD TWP",
    "CODORUS TWP",
    "CONEWAGO TWP",
    "DOVER TWP",
    "EAST HOPEWELL TWP",
    "EAST MANCHESTER TWP",
    "FAIRVIEW TWP",
    "FAWN TWP",
    "FRANKLIN TWP",
    "HEIDELBERG TWP",
    "HELLAM TWP",
    "HOPEWELL TWP",
    "JACKSON TWP",
    "LOWER CHANCEFORD TWP",
    "LOWER WINDSOR TWP",
    "MANCHESTER TWP",
    "MANHEIM TWP",
    "MONAGHAN TWP",
    "NEWBERRY TWP",
    "NORTH CODORUS TWP",
    "NORTH HOPEWELL TWP",
    "PARADISE TWP",
    "PEACH BOTTOM TWP",
    "PENN TWP",
    "SHREWSBURY TWP",
    "SPRING GARDEN TWP",
    "SPRINGETTSBURY TWP",
    "SPRINGFIELD TWP",
    "WARRINGTON TWP",
    "WASHINGTON TWP",
    "WEST MANCHESTER TWP",
    "WEST MANHEIM TWP",
    "WINDSOR TWP",
    "YORK TWP",

    // Census-designated places
    "EAST YORK",
    "EMIGSVILLE",
    "GRANTLEY",
    "NEW MARKET",
    "PARKVILLE",
    "PENNVILLE",
    "QUEENS GATE",
    "SHILOH",
    "SPRY",
    "STONYBROOK",
    "SUSQUEHANNA TRAILS",
    "TYLER RUN",
    "VALLEY GREEN",
    "VALLEY VIEW",
    "WEIGELSTOWN",
    "YORKLYN",

    // Unincorporated communities
    "ACCOMAC",
    "ADMIRE",
    "AIRVILLE",
    "AMBAU",
    "BANDANNA",
    "BERMUDIAN",
    "BIG MOUNTAIN",
    "BLACKROCK",
    "BROGUE",
    "BRYANSVILLE",
    "CLY",
    "CRALEY",
    "DAVIDSBURG",
    "DETTERS MILL",
    "FAYFIELD",
    "FIRESIDE TERRACE",
    "FOUSTOWN",
    "FUHRMANS MILL",
    "GATCHELLVILLE",
    "GLADES",
    "GLENVILLE",
    "GNATSTOWN",
    "HAMETOWN",
    "HANOVER JUNCTION",
    "HOPEWELL CENTER",
    "KRALLTOWN",
    "LEADERS HEIGHTS",
    "MACKEY FORD",
    "MOUNT ROYAL",
    "NEW BRIDGEVILLE",
    "NEW PARK",
    "NAUVOO",
    "ORE VALLEY",
    "PORTERS SIDELING",
    "SIDDONSBURG",
    "SPRING FORGE",
    "STOVERSTOWN",
    "STRINESTOWN",
    "SUNNYBURN",
    "TOLNA",
    "THOMASVILLE",
    "VALLEY FORGE",
    "VIOLET HILL",
    "WOODBINE"


  };
  
  private static final Set<String> MD_CITIES = new HashSet<String>(Arrays.asList(
      
    // Carroll County  
    "CARROLL COUNTY",
    "CARROLL CO",
    
    // Cities
    "WESTMINSTER",
    "MOUNT AIRY",

    // Towns
    "MANCHESTER",
    "NEW WINDSOR",
    "UNION BRIDGE",
    "HAMPSTEAD",
    "SYKESVILLE",
    "TANEYTOWN",

    // Census-designated place
    "ELDERSBURG",

    // Unincorporated communities
    "ALESIA",
    "CARROLLTON",
    "CARROLLTOWNE",
    "DETOUR",
    "FINKSBURG",
    "FRIZZELBURG",
    "GAMBER",
    "GAITHER",
    "GREENMOUNT",
    "HARNEY",
    "HENRYTON",
    "JASONTOWN",
    "KEYMAR",
    "LINEBORO",
    "LINWOOD",
    "LOUISVILLE",
    "MAYBERRY",
    "MIDDLEBURG",
    "MILLERS",
    "PATAPSCO",
    "PLEASANT VALLEY",
    "SILVER RUN",
    "UNION MILLS",
    "UNIONTOWN",
    "YOUNG MANS FANCY",

    
    // Frederick County
    "FREDERICK COUNTY",
    "FREDERICK CO",
    
    // Cities
    "BRUNSWICK",
    "FREDERICK",

    // Towns
    "BURKITTSVILLE",
    "EMMITSBURG",
    "MIDDLETOWN",
    "MYERSVILLE",
    "NEW MARKET",
    "THURMONT",
    "WALKERSVILLE",
    "WOODSBORO",

    // Village
    "ROSEMONT",

    // Census-designated places
    "ADAMSTOWN",
    "BALLENGER CREEK",
    "BARTONSVILLE",
    "BRADDOCK HEIGHTS",
    "BUCKEYSTOWN",
    "JEFFERSON",
    "LIBERTYTOWN",
    "LINGANORE",
    "MONROVIA",
    "POINT OF ROCKS",
    "SABILLASVILLE",
    "SPRING RIDGE",
    "URBANA",

    // Unincorporated communities
    "CLOVER HILL",
    "DISCOVERY",
    "GARFIELD",
    "GRACEHAM",
    "GREEN VALLEY",
    "IJAMSVILLE",
    "KNOXVILLE",
    "LADIESBURG",
    "LEWISTOWN",
    "LAKE LINGANORE",
    "LINGANORE",
    "NEW MIDWAY",
    "PETERSVILLE",
    "ROCKY RIDGE",
    "SPRING GARDEN",
    "SUNNY SIDE",
    "TUSCARORA",
    "UTICA",
    "WOLFSVILLE",

    // Washington County
    "WASHINGTON COUNTY",
    "WASHINGTON CO",
    
    "BOONSBORO",
    "CASCADE",
    "CLEAR SPRING",
    "FUNKSTOWN",
    "HANCOCK",
    "HAGERSTOWN",
    "KEEDYSVILLE",
    "SHARPSBURG",
    "SMITHSBURG",
    "WILLIAMSPORT"
  ));
  
  private static final Properties MISTYPED_CITIES = buildCodeTable(new String[]{
    "BERWICK",         "BERWICK TWP",
    "CARROL TWP",      "CARROLL TWP",
    "CICKINSON TWP",   "DICKINSON TWP",
    "COOK TWP",        "COOKE TWP",
    "DICKSON TWP",     "DICKINSON TWP",
    "EMITTSBURG",      "EMMITSBURG",
    "EMMITTSBURG",     "EMMITSBURG",
    "EMTTISBURG",      "EMMITSBURG",
    "GILFORD",         "GUILFORD TWP",
    "GILFORD TWP",     "GUILFORD TWP",
    "GREEN TWP",       "GREENE TWP",
    "HEIDELBURG TWP",  "HEIDELBERG TWP",
    "HEIDLEBERG TWP",  "HEIDELBERG TWP",
    "HEIDLEBURG TWP",  "HEIDELBERG TWP",
    "MONT ALOT",       "MONT ALTO",
    "MOUNT ALTO",      "MONT ALTO",
    "MOUNT HOLLY",     "MOUNT HOLLY SPRINGS",
    "MOUNT HOLLY BORO", "MOUNT HOLLY SPRINGS",
    "PARADISE",        "PARADISE TWP",
    "QUINCEY TWP",     "QUINCY TWP",
    "ROCKEY RIDGE",    "ROCKY RIDGE",
    "SOUTHMIDDLETON TWP", "SOUTH MIDDLETON TWP",
    "WAS",             "WASHINGTON TWP",
    "WASHTINGTON TWP", "WASHINGTON TWP",
    "WASHINTON TWP",   "WASHINGTON TWP",
    "WASHTONTON TWP",  "WASHINGTON TWP",
    "WEST MANHEIM",    "WEST MANHEIM TWP"
  });
}
