[33mcommit e91692a7bfe01f61d1e8246534477c140bdb1434[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 14 19:28:15 2018 -0700

    Fixed parsing problem with Accomack County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAccomackCountyParser.java
M	cadpage-private

[33mcommit a1a38a21fdba75e54d43d69e57583765f7f1b4de[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 14 17:33:25 2018 -0700

    Fixed parsing problem with hennepin County, MN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNHennepinCountyParser.java
M	cadpage-private

[33mcommit 2e7fffaa7bc38316e191b7e632d5c7be15b88c65[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 13 20:16:40 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit b81d27606e0e8067b518b44438e87e184bcf5043[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 13 19:57:37 2018 -0700

    Update sender filter for Etowah County, AL (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALEtowahCountyBParser.java
M	cadpage-private

[33mcommit 2474516213132e0412f82eeab070570bb2d6c71c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 12 22:09:36 2018 -0700

    Update sender filter for Fremont COunty, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COFremontCountyParser.java
M	cadpage-private

[33mcommit 16bd6666c11a0c29b49de9980999b32aaed30952[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 12 22:05:38 2018 -0700

    Fixed parsing problem with Calcasieu Parish, LA (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LACalcasieuParishAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LACalcasieuParishBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LACalcasieuParishParser.java
M	cadpage-private

[33mcommit d25465ca621a1456c559255f37ba368e6b7d62be[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 12 17:31:01 2018 -0700

    Update sender filter for St James Parish, LA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStJamesParishParser.java
M	cadpage-private

[33mcommit fa2e73b12ce664ec4a16f72f0dc4c20dc0d8ac15[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jun 11 01:58:49 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit cbfe62ec4def3147b41f2d68bfcdd4dd2a62e17e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 10 19:29:33 2018 -0700

    New Locationn: Lac Qui Parle County, MN
    And merged a lot of parsers using DispatchA43Parser as a base class

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNCrowWingCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNHubbardCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNIsantiCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNKandiyohiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNLacQuiParleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNLeSueurCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMcLeodCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMowerCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNNicolletCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNPenningtonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNPopeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRamseyCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRedwoodCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRenvilleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRoseauCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNScottCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNSibleyCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNStevensCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA43Parser.java
M	cadpage-private

[33mcommit cf1f8af48db9924e8a1782f15cc95a41400079c6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 19:00:47 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit d0047e0ee5b70a5ad69078ba47fb1625f1efe6a8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 18:48:06 2018 -0700

    Fixed parsing problem with Canmore, AB

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABCanmoreParser.java
M	cadpage-private

[33mcommit 4e5437e44d2a8c402c4a940d4e3b606319600816[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 18:33:49 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 0c74f03adc20c75085b197b60ae3770fc3f7ce5c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 17:39:04 2018 -0700

    Fixed parsing problem with Noble County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLouisvilleParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHNobleCountyParser.java
M	cadpage-private

[33mcommit 91cf9d2783956faf5832e28dfad08cab6e8dd808[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 17:06:05 2018 -0700

    Fixed possible problem with Montgomery County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMontgomeryCountyBParser.java
M	cadpage-private

[33mcommit 32bdcadf53e64c3edb1ebef76840d4578443d571[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 15:55:46 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 5fabea09f39484c795982c9ac4a733d26feff626[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 13:33:26 2018 -0700

    Release v1.9.15-54

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaAParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit bb5e44390d3083ba607fd57afd610a45bf62e519[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 09:34:51 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit f4a8231bb99e3f04ee41ddd70bda2a455347792a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 09:30:47 2018 -0700

    Fixed parsing problem with Kosiusko County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INKosciuskoCountyParser.java
M	cadpage-private

[33mcommit 992cb090261b9c13685cc33b3972dd8275a82610[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 8 08:38:09 2018 -0700

    Fixed parsing problem with West Baton Rouge Parish, LA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA13Parser.java
M	cadpage-private

[33mcommit a650b02d4b39673d7e768f3be7a444b7e47dd967[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jun 8 01:41:50 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit c7cde4debd86da0884a717f282a1e9c44739df39[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 7 21:35:38 2018 -0700

    Fixed parsing problem with Sumner County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSumnerCountyBParser.java
M	cadpage-private

[33mcommit 9dd826ad0330d234ab4d54c4e486fec1dbe57d92[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 7 19:32:20 2018 -0700

    Update genome.log

M	cadpage
M	cadpage-private

[33mcommit e9b15f03c5904a0d17ce3d867c52fb5e634a306f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jun 7 02:01:07 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit da1c831398fb8a5e91975c8a7a7dbe3419db6bca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 21:12:35 2018 -0700

    Fixed mapping problem with Benton County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORBentonCountyParser.java
M	cadpage-private

[33mcommit 545913ad8c0c087d55cfef8123eef29c2d51ca52[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 18:55:07 2018 -0700

    Update a911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaCParser.java
M	cadpage-private

[33mcommit 0539ef3ba18b2723ce3ed13099f927d4040a1ecd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 18:29:35 2018 -0700

    New Location: Sussex County, MA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MA/MASussexCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 7287666089a0536ccef5e406e2d76ca3a4e4bea5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 18:09:39 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 75b3e613b22252119a364facbb8b3970140aba15[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 14:08:26 2018 -0700

    New Location: Chugiak, AK

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AK/AKChugiakParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit f937e8306b5e9732638c0d5b6b2019246c706fcb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 13:39:36 2018 -0700

    New Location: Owen County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INOwenCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 07bf29bd3e048723c026b7712d27cb6a94e38bff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 12:43:44 2018 -0700

    New Location Ocean County, NJ (E)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyParser.java
M	cadpage-private

[33mcommit e1b8a55461046f4645a82275b05ba4308e818d01[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 08:28:15 2018 -0700

    Release v1.9.15-53

M	build.gradle
M	cadpage

[33mcommit 9d8af8da56675e12814dc80e5198a3c522ef68ec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 6 08:04:33 2018 -0700

    Update GPS lookup table for Knox County, OH

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHKnoxCountyParser.java
M	cadpage-private

[33mcommit 5cac3f5e4481ef7eb6676b5705431a492836a48a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Jun 6 01:55:12 2018 -0700

    general updates.

M	cadpage
M	cadpage-private
M	docs/support.txt

[33mcommit 9ae4ddcf6ac356a9cefcf4636f7f0d95a99c4888[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 5 22:55:35 2018 -0700

    Update sender filter for Bradford County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABradfordCountyParser.java
M	cadpage-private

[33mcommit 8fc4bef4828008860db357dc5868045a9e7e4331[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 5 22:51:47 2018 -0700

    Update sender filter for OHSummitCountyE

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyEParser.java
M	cadpage-private

[33mcommit f6986a99fa7047c25c8bfd65f0309f41bcac65b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 5 22:35:32 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyBParser.java
M	cadpage-private

[33mcommit 0cc7e2ff0592dad9e82492661714569469ee2e29[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 5 10:15:35 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 9dd1226dbfa68be51cbe001afece99960d65f91c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 4 19:34:55 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 77c379084ead7c54df2dd82c7012e4f85e87b3ba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 4 19:29:36 2018 -0700

    Fixed parsing problem with Queen Annes County, MD (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyBParser.java
M	cadpage-private

[33mcommit db828758edee1874366d410e1620c045e94dc591[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 4 19:19:25 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COGrandCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONSimcoeCountyAParser.java
M	cadpage-private

[33mcommit 80c907def741faaaee77dc2822d20287c7011bef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 4 18:33:01 2018 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceAParser.java

[33mcommit 6860b62853f939dd0b8804d7e54c8e2f0d3d3100[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 4 18:28:40 2018 -0700

    Fixed parsing problem with Acadian Amulance, TX

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/XXAcadianAmbulanceParser.java
M	cadpage-private

[33mcommit 9e956e8d3f3cef76882e129f5d92d2aafd21120f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 4 12:46:08 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit a7b08cc6e70192d0c4969f7c85b3874a1258b66a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 3 17:54:04 2018 -0700

    Cleaned up parsers, update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyJParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBrunswickCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFloydCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA71Parser.java
M	cadpage-private

[33mcommit 2441d0d7855754c6ddbe15abf2a7ce63d2a88d3c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 3 16:21:27 2018 -0700

    New Location Oconee County, SC (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOconeeCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOconeeCountyParser.java
M	cadpage-private

[33mcommit e11b14df24b4934974287c58a2b9d35a5ba4a252[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 2 11:12:24 2018 -0700

    Release v1.9.15-52

M	build.gradle
M	cadpage

[33mcommit 17048f98b45fcf869cdfa7c39da0736cf26d6ca4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 2 10:31:57 2018 -0700

    Fixed parsing problem with Macon County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyBParser.java
M	cadpage-private

[33mcommit 89625451edcefdf5ee4b106fa3602ee7941edb99[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 2 09:09:34 2018 -0700

    Release v1.9.15-51

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONSimcoeCountyBParser.java
M	cadpage-private

[33mcommit 25181f728b9a2996a2794d07026d652d66c93942[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 2 08:00:39 2018 -0700

    Added more cities to Buffalo, NY

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBuffaloParser.java
M	cadpage-private

[33mcommit 6304f401f7b8ec035b42a1d86976f0343dfa5ef8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jun 2 02:34:42 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit 66c493607d8a627574f73c7c447ba63e83a62582[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 1 22:25:29 2018 -0700

    Update sender filter for Barton County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSBartonCountyParser.java
M	cadpage-private

[33mcommit 996a3559a63feae68432cb0a41d20c28643f5174[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 1 21:59:05 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 6d6ae68aa86f024f852a99b8c8f12cca1ca17c4c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 1 21:35:45 2018 -0700

    Fixed pasing problems with Simcoe County, ON (Add B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSaintMarysCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONMississaugaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONSimcoeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONSimcoeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONSimcoeCountyParser.java
M	cadpage-private

[33mcommit 94b2eea544d8d6e2a570886942df4848775fd01a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 1 20:02:57 2018 -0700

    Fixed parsing problem with Buffalo, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBuffaloParser.java
M	cadpage-private

[33mcommit c37d6edd60b7749962e31ed2072d524ade75a742[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 31 16:16:55 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 1235c2e52416d973a5340111789c74e193bec96f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 30 10:19:28 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit aa985e4485f0f242a27503b9ca8c3bfa1948d918[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 30 10:18:38 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 74e680a62218d6da241bc06c08620f077e5fa084[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 30 10:08:24 2018 -0700

    Fixed parsing problem with Lee County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLeeCountyParser.java
M	cadpage-private

[33mcommit 13fecf3f69603305ec2da4c1ef19da727b435a4f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 29 17:33:24 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 22f0db22dbd736c9beaf6575033ec1f2d28b6ee1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 29 17:26:52 2018 -0700

    Update sender filter for Decatur COunty, GA (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADecaturCountyBParser.java
M	cadpage-private

[33mcommit 29589ac0c8d4b0f6747cd989a9d5d0d9e31a4c83[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue May 29 01:38:19 2018 -0700

    general updates.

M	cadpage

[33mcommit 47f02710b0f3437ea5acd5c70531beb1e834903e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 18:12:30 2018 -0700

    Release v1.9.15-50

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZYavapaiCountyAParser.java
M	cadpage-private

[33mcommit 6ef64ea1b97a85709be6df8c4878107f70a5ef7a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 16:48:22 2018 -0700

    Updated tables for Marion County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMarionCountyParser.java

[33mcommit d6135f93810abc5f88fb0e5a4954373b97dbd829[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 16:43:35 2018 -0700

    Updated tables for West Feliciana Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAWestFelicianaParishParser.java
M	cadpage-private

[33mcommit bc99c813c97b3476cbd5238cee6baad014356c26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 16:09:53 2018 -0700

    Fixed parsing problem with Marion County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMarionCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA48Parser.java
M	cadpage-private

[33mcommit 5294d029291ac77700fb81f8c80db3695a6b748a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 14:58:17 2018 -0700

    Parsing problem with new Haven County, CT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
M	cadpage-private

[33mcommit 119063301380faa80d5b9a6a6dbd2cb0d9d7d2c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 11:23:22 2018 -0700

    Update sender filter for Pulaski County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPulaskiCountyParser.java
M	cadpage-private

[33mcommit b7ada315ff2a867675fc389836708f3f68408d68[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 11:19:16 2018 -0700

    Fixed parsing problem with Noble County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHNobleCountyParser.java
M	cadpage-private

[33mcommit 3160e188b504636354b4e601b383d1e4c6368a45[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 26 11:04:57 2018 -0700

    Fixed parsing problem with Midland County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMidlandCountyParser.java
M	cadpage-private

[33mcommit 28c8922ff5af8dec647666ffb664c8bc196ed6af[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 23:23:25 2018 -0700

    Fixed parsing problem with Story County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAStoryCountyParser.java
M	cadpage-private

[33mcommit 26074afb68ef750301520e5b1caab80a2310402c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 21:24:01 2018 -0700

    Fixed parsing problem with Macon County, NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyBParser.java
M	cadpage-private

[33mcommit 7b277e1d6c1838374e01aad6773a996c0842f010[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 20:28:54 2018 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 9d37b64c6ac13c6ac9cd887eb0c924aa2ddfb247[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 20:15:38 2018 -0700

    Fixed parsing problem with Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 0db9e7209518c5f56d8fd834f1818b325337cba6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 19:40:28 2018 -0700

    Updage genome.log

M	cadpage-private

[33mcommit 01ae82ef7252976681c7f758ced1334cc6652992[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 19:34:59 2018 -0700

    Fixed parsing problem with Queen Annes County, MD (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyParser.java
M	cadpage-private

[33mcommit d286c72953c38bc620f1d1706fe77ec55b66dffe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 12:15:11 2018 -0700

    New Location: Orange COunty, VT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTLamoilleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTOrangeCountyParser.java
M	cadpage-private

[33mcommit f3505a1ade1386cb7525ea0b7388153aae7e7838[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 25 11:58:41 2018 -0700

    Fixed parsing problem with Dorchester County, MD

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDDorchesterCountyParser.java
M	cadpage-private

[33mcommit 2d446d8eec6d5f46d16cfd521777240570bcf7c6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri May 25 01:48:01 2018 -0700

    general updates.

M	cadpage
M	cadpage-private
M	docs/support.txt

[33mcommit 26a8a6241c19c7fde69f08d0aaf90a479d9980d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 24 21:21:57 2018 -0700

    Fixed parsing problem with Charles County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCharlesCountyAParser.java
M	cadpage-private

[33mcommit 757d5d183277ebbf82266e9745dcc994b54e9f58[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 24 19:47:07 2018 -0700

    Update GPS lookup table for Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 211d0ed42297dc83a3fc026b45d82a2b1be739bb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 24 18:23:49 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit d6c780ff4cb02970109bc4267d36fb7c5582b7e7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 24 18:04:28 2018 -0700

    Fixed parsing problem with San Joaquin County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanJoaquinCountyParser.java
M	cadpage-private

[33mcommit 8f0f51ff4eff44d2f210de3e50f45eefdfc39af0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 24 13:04:40 2018 -0700

    Release v1.9.15-49

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 0509970aca283998e7ed58f229b84ebdbdb66994[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 24 11:32:40 2018 -0700

    New Location Dekalb County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MODekalbCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 63f08b21326d0493406c5d67fdcb2ae2db4922c2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 24 09:56:01 2018 -0700

    Parse adjustment for Snohomish County, WA (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyBParser.java
M	cadpage-private

[33mcommit 349ffd91b08599dca0eb0a239d832e4b222c24ca[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu May 24 01:19:43 2018 -0700

    general udpates.

M	cadpage

[33mcommit 6722bf95b4315066baf793d406938f2523743eeb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 23 21:22:40 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 244ed54b493303a0caf8437f6e40f46a623b132c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 23 21:06:21 2018 -0700

    Update sender filter for Mahoning County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyAParser.java
M	cadpage-private

[33mcommit 11b424f2020b1db6b824af41b4b49b8598eb43c9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 23 20:45:17 2018 -0700

    Fixed parsing problem with Berks County, PA (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABerksCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABerksCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABerksCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
M	cadpage-private

[33mcommit 64a70196b48ef46e82e51eb7d69296c0b13e31ac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 23 14:41:08 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 737189e71113e325bad7b7343fdb08c3a9b7b5ad[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 23 14:32:09 2018 -0700

    Update GPS lookup table for Marion County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit e62e2b1096a702098dabc002142d640459bb2b87[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 23 14:24:50 2018 -0700

    Updage genome.log

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyCParser.java
M	cadpage-private

[33mcommit a450929dc153d9a7af97bbaf4c87d1d45317cdc8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 23 12:52:23 2018 -0700

    Fixed parsing problem with Washington County, OR (C)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyCParser.java
M	cadpage-private

[33mcommit 69aa031859eaf1a7a536d8ee5cd6a478657a2293[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 23 00:50:06 2018 -0700

    general updates.

M	cadpage

[33mcommit da5b54657fb062fe49bb1a04b91ceadb61916f7d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 22 21:53:15 2018 -0700

    Update sender filter for O Fallon, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILOFallonParser.java
M	cadpage-private

[33mcommit b1f0331f8f318c4afa5fdb44c8e1b46f8cb2b43d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 22 19:56:56 2018 -0700

    Fixed parsing problem with Muskingum County, OH (Add C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMuskingumCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMuskingumCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH04Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH05Parser.java
M	cadpage-private

[33mcommit f63bb0859be321ee1f7a057a59ad2f864162acc5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 22 13:59:14 2018 -0700

    Release v1.9.15-48

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyBParser.java

[33mcommit 5a02357d01962599522e0b0468b1522e7849ef81[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 22 12:58:09 2018 -0700

    Fixed parsing problem with Buffalo, NY

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBuffaloParser.java
M	cadpage-private

[33mcommit 2b3dc16a421fce5dcf6fd6a7fbdd73ee79e864bb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue May 22 01:30:20 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit b9d0a734f65d7d35efdf4274ba5ef769cfc79d67[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 21 15:55:21 2018 -0700

    Fixed parsing problem with Buffalo, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBuffaloParser.java
M	cadpage-private

[33mcommit 7285680857648fa30f9f68f9840d3b129d3f9bb8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 21 13:08:16 2018 -0700

    Release v1.9.15-47

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit a98ca73f359afa2580f37974ca6d89d66c686ac2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 21 12:38:09 2018 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyBParser.java
M	cadpage-private

[33mcommit 26d16d33a66227854691fbc53b612bbfee448fcd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 21 10:57:41 2018 -0700

    Fixed parsing problem with Macon County, NC (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyParser.java
M	cadpage-private

[33mcommit 4a6ff2d90114d5adc0f6e1d48b192896609f6cff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 21 08:58:12 2018 -0700

    Fixed parsing problem with Salt Lake County, UT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyBParser.java
M	cadpage-private

[33mcommit e9d044c6932d6da2c3953ee5765ad2fee1f8e088[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 21 08:11:44 2018 -0700

    New Location: Pike County, AL

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALPikeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 5ae9145e1c02ade07e48927922701f7acaaa6789[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 20 20:29:42 2018 -0700

    Fixed mapping problem with Benton County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORBentonCountyParser.java
M	cadpage-private

[33mcommit 1eabaa44917bab1e07b2631aacaa73eb041342f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 20 10:39:55 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1e157a09bca2990f41d81f931c7499baede60923[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 19 08:34:30 2018 -0700

    Update call table for DeSoto County, MS (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyAParser.java
M	cadpage-private

[33mcommit ec81842dc8666526a13efc6758567f29d04b422e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 18 16:11:35 2018 -0700

    Release v1.9.15-46

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTollandCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit ada74ccf2f9befd518f674b654015fbe8a4e1109[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Fri May 18 15:41:25 2018 -0700

    skeletons

M	cadpage-private

[33mcommit a2ea59438252fa6cbff843e76d1b5703872dd6f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 18 15:01:09 2018 -0700

    Fixed parsing problem with Montgomery County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMontgomeryCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit af3663dcc8a3cd76b4d430f12aec0c7dd85d1691[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 18 14:14:39 2018 -0700

    Fixed parsing problem with Tolland County, CT (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTollandCountyAParser.java
M	cadpage-private

[33mcommit 29d4362989c8a2b3dfaf913ae6dd0a8bd7df29c1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 18 13:27:39 2018 -0700

    Fixed parsing problem with Salt Lake County, UT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyBParser.java
M	cadpage-private

[33mcommit 8620cff0025659d0217f5b8d767f2d4a899b4fb9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 17 22:20:16 2018 -0700

    update genome.log

M	cadpage-private

[33mcommit ab1783d91143693df7257224543d8e6ab8a65208[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 17 21:46:45 2018 -0700

    Fixed parsing problem with San Joaquin County, CA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanJoaquinCountyParser.java
M	cadpage-private

[33mcommit 9fa95081b089e4606aa90ba41b3d11edb5b4eea4[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 16 23:59:24 2018 -0700

    general udpates.

M	cadpage
M	cadpage-private

[33mcommit de80221cae7c182a5bb0feec97c4fb873f945608[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 16 20:10:31 2018 -0700

    Update sender filter for Parke County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INParkeCountyParser.java
M	cadpage-private

[33mcommit 03fbc72083356bf6c6a017cff868013509fa163f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 16 20:03:32 2018 -0700

    Updated A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 93ff21390d286f767911cd1a1850474db3287f61[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 16 00:41:16 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit 9bb4ba7ac1d1046387fa71dcc8c337b73b27cf35[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 15 15:04:24 2018 -0700

    Release v1.9.15-45

M	build.gradle
M	cadpage

[33mcommit 4f8b7747c83e113b0da797a91910af668fba21e7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 15 14:36:23 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 806a0dd813b70e86bdb33c959f8d661e66c37bdb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 15 14:16:14 2018 -0700

    Fixed parsing problem with Northumberland County, PA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthumberlandCountyBParser.java
M	cadpage-private

[33mcommit c9712bada56dbd93661a694f3bfbc2e06bc62191[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 15 13:50:49 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit e76cf05e3e530df4a360cf7a509862cd82fc3ee6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 15 13:20:55 2018 -0700

    Fixed parsing problem with Armstrong County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAArmstrongCountyBParser.java
M	cadpage-private

[33mcommit 189d197bd653b43c65ce0ece30f39416266b56f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 15 10:51:44 2018 -0700

    Fixed parsing problem with Buffalo, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBuffaloParser.java
M	cadpage-private

[33mcommit 7fae3493cbac8588c8d57078db560b44df64a276[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 15 09:09:32 2018 -0700

    Fixed parsing problem with Humboldt County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAHumboldtCountyParser.java
M	cadpage-private

[33mcommit 4a34ac855382f304d3a6666e69bea5e4b532d427[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 14 16:20:41 2018 -0700

    Update msg doc

M	cadpage
M	cadpage-private

[33mcommit f6e3757e5d491642b11f77ce668e4a841c695066[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon May 14 01:09:17 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit a48361423c534a86ac03940ffa73bba50e9e11bc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 17:32:19 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 2cbb031020f859e6294856ec01d1f7a4b71c5faa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 17:18:02 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 41d9913387c2b6639a3838557dbbec1def5d1ce2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 17:10:18 2018 -0700

    Update sender filter for Union County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCUnionCountyParser.java
M	cadpage-private

[33mcommit 2028653921e2c181968aa33c5e3742d818803366[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 16:30:22 2018 -0700

    Update GPS Lookup table for Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 4f3c840bc38d412e5aaf684e0b6de551c85ba1f8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 16:06:46 2018 -0700

    Fixed parsing problem with Jefferson COunty, AL (J)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyJParser.java
M	cadpage-private

[33mcommit 51ec2a36b351eaae0bcc7db65963354ab25d2a7d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 15:37:02 2018 -0700

    Fixed parsing problem with Ray County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MORayCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyAParser.java
M	cadpage-private

[33mcommit 09e674fd59abc5f89fb9c7244dad66b59464aae3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 10:24:09 2018 -0700

    Update sender filter for  White County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWhiteCountyParser.java
M	cadpage-private

[33mcommit 012326c71547ac79d48300c160be21895bd5c397[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 13 09:50:56 2018 -0700

    Fixed parsaing problem with Rock County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIRockCountyParser.java
M	cadpage-private

[33mcommit e510966fce227189b7ed3001016342b30367d88b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 12 09:09:19 2018 -0700

    Release v1.9.15-44

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 500deb7b568c20df94bc921d44bc00725a38fe77[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 12 08:36:53 2018 -0700

    UKpdate GPS lookup table for MarionCounty, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit 53f879eaccb038a7a4701e83f6ccd003eff20dfb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 11 20:46:22 2018 -0700

    Update sender filter for Loudon County, TNN (again)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNLoudonCountyParser.java
M	cadpage-private

[33mcommit 710129b15fb9ee6fb201d9f9d51a381da06735f5[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Fri May 11 15:54:58 2018 -0700

    skeletons

M	cadpage-private

[33mcommit def3262149fa7471a9d85382504f5eb5ad617bcb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 11 12:43:38 2018 -0700

    Updated A911 Parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 694ab229a1aea663993942078639b305cf550621[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 11 10:27:14 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1f74dc763122a35168c5a319febfd57e8dee6a41[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 10 20:55:09 2018 -0700

    resync

M	cadpage

[33mcommit 7dc270a4158c9cd38c809133845647f626323483[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu May 10 01:41:17 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit 9a17609cb1be467fd42c7fed2650513e9231d872[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 9 12:36:52 2018 -0700

    Update msg doc

M	cadpage
M	cadpage-private

[33mcommit de098fee84151d71af2558e6f53af8af6678d4eb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue May 8 01:14:21 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit b7d0463122f4fd6e8995d8aa0af270981438ce21[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 7 20:32:48 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 8dcae23485a5fb88c0c7df4c3ad3e5d0818d557e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 7 19:59:21 2018 -0700

    Parsing problem with Henry County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHenryCountyParser.java
M	cadpage-private

[33mcommit 329dac64b4008b485bab17cfa18c68a6d1a4886b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 7 19:31:47 2018 -0700

    Fixed parsing problem with DeSoto County, MS (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyAParser.java
M	cadpage-private

[33mcommit 1dc8fc8e964bff9e83f0ca0cbe3626cbbb39bd36[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 15:00:35 2018 -0700

    Release v1.9.15-43

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWapelloCountyParser.java
M	cadpage-private

[33mcommit 3f6a299ca5d6236765892416fd1161045bd0ef45[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 13:51:20 2018 -0700

    New Location: Marlboro County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 57b8cdd365eeda6735569c21739daa14e300c7f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 13:37:43 2018 -0700

    New Location: Hill County, MT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 41e177dd03248d448b1fb0cd2dd6c7e83699fca6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 12:40:43 2018 -0700

    New Location: Winnebago County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 41a0c5148919b1eb26d2e5ec93cb0f6621966442[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 12:09:59 2018 -0700

    New Location: Sumner County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 48e627cf3fb14665d60b881126abf670891c42f0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 12:02:38 2018 -0700

    New Location: Morgan County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit d7d0266055578ee2ef8ede9212f7e8fc9cfa2d81[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 11:59:53 2018 -0700

    New Location Wapello County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit a8c8a9fee62abcb9197cc1f822198357e8c95435[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 11:10:52 2018 -0700

    Fixed parsing problem with Marion County, KS (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMarionCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMarionCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMarionCountyParser.java
M	cadpage-private

[33mcommit c6cf04dd484ad73030a9a24a7fb26270f692b30f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 10:03:49 2018 -0700

    Parsing problem with Allegheny County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-private

[33mcommit 83e83fa7757531d22f1eb5001b50196f4bafef72[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 5 09:58:38 2018 -0700

    Fixed parsing problem with Mecosta County, MI

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMecostaCountyParser.java
M	cadpage-private

[33mcommit 779720282d8ba2ef332e068dcd741fa9858b5fa2[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat May 5 00:07:40 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit 433ef969692944748f27165f52ab7013229554d2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 4 20:23:55 2018 -0700

    Update sender filter for Cuyahoga County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-private

[33mcommit 77c234d4f2ebb17bc7a65fff30e6c6cee3b7f283[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 4 20:19:20 2018 -0700

    Fixed parsing problem with Pierce County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-private

[33mcommit bbe3f970badfdd01ed29bed701197e52cccdd7f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 4 17:30:45 2018 -0700

    Fixed parsing problem with Monroe County, PA (Add B)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMonroeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMonroeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMonroeCountyParser.java
M	cadpage-private

[33mcommit 3a05a22a8f743ee8a734b71082aa204e4e706cb6[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Fri May 4 15:48:07 2018 -0700

    skeletons

M	cadpage-private

[33mcommit 654477e69faf7d502004bc86efe19394ae2f75f0[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri May 4 03:37:14 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit 987244aa80111d1ad3f354c4ab430f2cfbe06c80[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 3 21:44:12 2018 -0700

    Release v1.9.15-42

M	build.gradle
M	cadpage

[33mcommit cc6e5fe8a342fae3b849d3e4eb41271da3d26afd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 3 19:26:22 2018 -0700

    Update Sender Filter for Marshall County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMarshallCountyCParser.java
M	cadpage-private

[33mcommit 9a5e535e6ba732e3a383734f11b64361e0e762d1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 3 18:58:16 2018 -0700

    Update GPS lookup table for Dane County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIDaneCountyParser.java
M	cadpage-private

[33mcommit 06bd7fb20fb70b6f3f90c13f882fc92fb4101527[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 3 18:52:32 2018 -0700

    Update A911 Parser Table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 311213c9a938f91342c0e4643b0efd6be800b847[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 3 18:43:38 2018 -0700

    Update sender filter for Nacogdoches County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNacogdochesCountyParser.java
M	cadpage-private

[33mcommit a36f1e0c2d05913f1d98b0bfbff6a32d9e6410bd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 3 18:35:46 2018 -0700

    Fixed parsing problem with Roanoke County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCountyParser.java
M	cadpage-private

[33mcommit 141f0970980ee64f0b9aee0a428caef994a06f3a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 3 15:04:30 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 8121529e3bb85c7c45b6cc4e8b53aeb5d0b0bb86[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 2 18:32:29 2018 -0700

    Update sender filter for Crawford County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACrawfordCountyParser.java
M	cadpage-private

[33mcommit 2b6e66e90caa34b2cf8e88ddb838f8deb97cbfe4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 2 18:23:48 2018 -0700

    Synchronize everything

M	cadpage

[33mcommit 58ad80f4d61a4d868efda071422dac747955ca5c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 2 02:35:03 2018 -0700

    general updates.

M	cadpage

[33mcommit 1d27f102f5a694770d0c71e1210cdaf9db3d79e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 1 20:02:41 2018 -0700

    New Location: Rio Blanco County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 36b27323d4f6f2c1b02ac7ab1734c33b154300d4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 1 19:55:52 2018 -0700

    New Location: Colquitt County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit b01ea15e98fa515454dc22209e9fcb487b0112e0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 1 19:17:10 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 3b096012f628127e9462d34adeb07d53b22aeec3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 1 13:07:01 2018 -0700

    New Location: Knox County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 93229ae6ed19eceba0c5cb3461febef49512f742[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 1 11:02:03 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit b800b6e2b62701d99d998e18ba06767300d9f80f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 1 10:23:57 2018 -0700

    Update msg doc

M	cadpage
M	cadpage-private

[33mcommit cb2545c6695357a1319c5128ec01514b9ad4db6f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue May 1 01:48:20 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit 62a796c25bcb552efffa0e06a322209f9bc0182b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 30 17:36:16 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 84ce141bf005878bf890f1c0f2e85d34a6f58986[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 30 08:49:49 2018 -0700

    Release v1.9.15-41

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 94edeaf989f1611ef07aa012f78048fe881d9abd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 29 16:56:37 2018 -0700

    New Location: Berkeley County, SC"

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 636d8a7908b79926ea5c7cf4ef4f8da65350d258[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 29 15:15:40 2018 -0700

    New Location: Allen Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 75a9e6b1667e0b694e25fead7ae111c56c88de85[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 29 15:11:14 2018 -0700

    New Location: Harding County, KY (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHardinCountyParser.java
M	cadpage-private

[33mcommit f50476116f88d9916908f3c184f1135acc91c031[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 29 15:02:51 2018 -0700

    New Location: Sand Springs, OK

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit bc6b35275eb8db0bb7401b4a756ff4339ce18ee7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 29 14:51:15 2018 -0700

    new location: Laporte County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 208e6438f76f9c954b4dc7fe29ca60ffba8efcbd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 29 14:41:51 2018 -0700

    Fixed parsing problem with Monroe County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMonroeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNLoudonCountyParser.java
M	cadpage-private

[33mcommit 432b9e5d33370c7f2bcb39aa503f4af7c4e7e6b8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 28 19:05:00 2018 -0700

    Fixed parsing problem with Boyd County, KY (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBoydCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBoydCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBoydCountyParser.java
M	cadpage-private

[33mcommit 491e35cfd951ed6fdbf211afbe8ec357df24fd22[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 27 17:43:55 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit d84beecb726fac90ab8a55123d99dde52fdc86bd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 27 17:36:10 2018 -0700

    Fixed parsing problem with Cascade County, MT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyBParser.java
M	cadpage-private

[33mcommit a4e4ff9f8898771b8b5ffa201d31e4905b16793f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 27 16:18:26 2018 -0700

    Update GPS lookup table for Saint Marys County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSaintMarysCountyParser.java
M	cadpage-private

[33mcommit 0d66cbcbc3b7a9eeaec25621a77f976f13fd367e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 27 16:10:50 2018 -0700

    Fixed parsing problem with Boulder County, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyBParser.java
M	cadpage-private

[33mcommit ce866a49703c11ab9f116f5b0c846f1715a39aa3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 27 14:18:28 2018 -0700

    Fixed parsing probelm with Franklin County, PA (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyBParser.java
M	cadpage-private

[33mcommit d3426e0b0e90acd74976de6d2decd9f3c0ae22a9[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Apr 27 03:43:44 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 112e6dd69c3330d42eb9e9b43b0db850516bbae3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Apr 26 03:05:38 2018 -0700

    general updates.

M	cadpage

[33mcommit c53f64a019bf80314d059bd60927aa646d412d75[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 25 19:02:04 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit f1c34d39ca7ee5de71f3fc29510945e16183d149[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 24 07:58:13 2018 -0700

    Release v1.9.15-40

M	build.gradle
M	cadpage

[33mcommit fb50f8decfe0dc080a15645c9531cb717294b985[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 24 07:31:49 2018 -0700

    Locked in legacy branch of Cadpage

M	cadpage

[33mcommit 64209b60ef102b5147b75aaaee4a3102534ddc3a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Apr 24 02:13:40 2018 -0700

    general updates.

M	cadpage
M	cadpage-private

[33mcommit 3570602f39bc94d0668bca4931c10a33753c94b3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 23 21:52:21 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit bccc517897ecf3f6366a56c75abba15d3d8a1f8e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 23 21:46:39 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 82b1a6cb174b7a17299bd6af2a55ff8e653a906b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 23 21:35:35 2018 -0700

    Fixed parsing problem with Santa Cruz County, CA (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaCruzCountyCParser.java
M	cadpage-private

[33mcommit 89eafb7d53da8fdefaa56f8f5fa0cffc6d6aa26a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 23 20:41:52 2018 -0700

    Fixed parsing problem with Jefferson County, CO (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyDParser.java
M	cadpage-private

[33mcommit d87d5ebd43d5742bb4978ddfc857b068192ec902[m
Merge: d16c234 fb25758
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 23 19:06:17 2018 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit d16c23476a9119493e1a70c20a376b4a180c4273[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 23 19:06:08 2018 -0700

    Merge latest changes

M	build.gradle
M	cadpage
M	cadpage-private
M	gradle/wrapper/gradle-wrapper.properties

[33mcommit fb25758b2d80e404f041e232c50a142e5c60229f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 22 15:06:10 2018 -0700

    Fixed parsing problem with Niagara County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNiagaraCountyCParser.java
M	cadpage-private

[33mcommit 856fb6c1d5f6efbcbd98a21e6b9ec8f141c599d0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 22 14:57:04 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNiagaraCountyCParser.java
M	cadpage-private

[33mcommit 09edd4b9bdcc017e3033c81f7c8a596d1ae24760[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 22 13:51:34 2018 -0700

    Release v1.9.15-39

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishCParser.java
M	cadpage-private

[33mcommit c95a77f82adaccce1386602a00ffcd63d90a2f51[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 22 12:35:57 2018 -0700

    Fixed parsing problem with Niagara County, NY (Add C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWakeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNiagaraCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNiagaraCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH03Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchOSSIParser.java
M	cadpage-private

[33mcommit 304f63e34c6bbaa103722e44d513a58fc04d732a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 22 10:47:52 2018 -0700

    Fixed parsing problem with Terrebonne Parish, LA (Add C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishParser.java
M	cadpage-private

[33mcommit 1ac1fa8f5f8a7e463104cdf0ea694eb1b9fe53a0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 21 19:10:56 2018 -0700

    new Location: Sebastian County, AR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARSebastianCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit ab891a142fbd90e9727fba952c731bcc63060861[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 21 17:40:15 2018 -0700

    Cleaned up city for Mobile couty, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMobileCountyParser.java
M	cadpage-private

[33mcommit 91a29da583901f669a1b6451111ba3c92bc53f00[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 21 17:25:00 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 9787d66ae6ae304a2642bc163f399c44c7b3e379[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 21 16:54:16 2018 -0700

    Fixed parsing problem with Brunswick County, NC (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBrunswickCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBrunswickCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBrunswickCountyParser.java
M	cadpage-private

[33mcommit 9885ede510abeedfbdf97b56421d087049575744[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 21 15:41:49 2018 -0700

    Parsing problem Wake County, NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Message.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWakeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchOSSIParser.java
M	cadpage-private

[33mcommit 343dcad706450394d8ac17ebaa49d1fda8ff7007[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 21 10:42:07 2018 -0700

    Fixed parsing problem with Cumberland County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCumberlandCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
M	cadpage-private

[33mcommit 107ac307d83aeae9d29d762dfe3b73f004b28ebe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 20 19:22:14 2018 -0700

    Fixed problem with all Dispatch H03Parser subclasses

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH03Parser.java
M	cadpage-private

[33mcommit aef9249472a079b2f68171c022e5befc08df0d43[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 20 09:21:50 2018 -0700

    Fixed stack recursion loop processing DOCTYPE messages

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-private

[33mcommit 565f7c986ac2a8f08b5d62cf9e7e70e77fc06ec3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Apr 20 01:48:01 2018 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit ebed92913420435585881a98b307f951943530a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 19 15:29:58 2018 -0700

    Fixed parsing problem with Clinton County, MO (Add B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCameronParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOClintonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOClintonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOClintonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBCParser.java
M	cadpage-private

[33mcommit dd13b95055a4260f875d73b9b210d79b623a68b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 18 15:36:35 2018 -0700

    UKpdate A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 468a8a2dff59901aefd538ee18b4fa4cec116673[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 18 15:16:46 2018 -0700

    Fixed pasing problem with Greenbrier County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVGreenbrierCountyParser.java
M	cadpage-private

[33mcommit 6123cbafa99798302ecfe4a5bfe1a0c383c1f36c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 18 14:11:28 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 40fddb8a20716c107f0f373bd24a0a9d2390f49b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 18 12:33:31 2018 -0700

    Fixed parsing problem with Livingston County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLivingstonCountyCParser.java
M	cadpage-private

[33mcommit 7ac2d1edb91b770a684a412659bb746b024a486d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 18 12:03:31 2018 -0700

    Added call code table for Carter County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarterCountyParser.java
M	cadpage-private

[33mcommit 878370c7417e9e4fbed636e87b138025895f4d9f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Apr 18 03:04:56 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 1120252dfd5fa9b302ddde99ed6cf52025f4ddf2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 17 22:10:54 2018 -0700

    update genome.log

M	cadpage-private

[33mcommit 1c5fb6dee85cfe92ffa26b6da97a0f7b33d85a78[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 17 13:35:22 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 28d4372f7092a5219109c98a6f710efb3a8e27ad[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Apr 17 01:40:09 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 6b94c5f64ff62ad4a44f7b09140dc5fcb53bae85[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 16 19:38:46 2018 -0700

    New Locations: Billings and Kidder County, ND

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java

[33mcommit 0ee7f64241df5a9312de57aead5d919a15560343[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 16 19:32:40 2018 -0700

    Update sender filter for Carter County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarterCountyParser.java
M	cadpage-private

[33mcommit 8ce1174c5a67783c08130139ec77414907140f7d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 16 19:29:51 2018 -0700

    Update sender filter for Macomb County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMacombCountyParser.java
M	cadpage-private

[33mcommit 6fd832a86c5cbb5b9e1fac3eda63e25369a6b325[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 16 14:37:57 2018 -0700

    Release v1.9.15-38

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit c3e759d24a337af32cfe41afef7c2fdf2a6b5dea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 16 13:18:08 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 5a83d4eec96b1f1f727652f9e87c6fc97950f2ae[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 16 13:08:45 2018 -0700

    Update sender filter for Shelby County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALShelbyCountyParser.java
M	cadpage-private

[33mcommit 223b68a3fcf584f7f1f28901a5cf7f8dacac4cfe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 16 13:03:10 2018 -0700

    Fixed parsing problem with Royse City, TX (add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRoyseCityAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRoyseCityBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRoyseCityParser.java
M	cadpage-private

[33mcommit 203804bb64a9e936efd56a5d233c13e55621b630[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 22:12:55 2018 -0700

    KUpdate msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
M	cadpage-private

[33mcommit 42c97464e4a78e6871768ff125f994f7e7889302[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 21:59:53 2018 -0700

    KUpdate msg doc

M	cadpage-private

[33mcommit 5912bf91dc198fd4ab1164a72f86d9c4b250b023[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 21:40:43 2018 -0700

    Fixed parsing problem with King County, WA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyDParser.java
M	cadpage-private

[33mcommit b3d17a26bade5d296620b5c782c81ce502c902a7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 20:15:53 2018 -0700

    Fixed parsing problem with Snohomish County, WA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyBParser.java
M	cadpage-private

[33mcommit c513126df6c3f70a555fa4170a388519ff07bdda[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 18:31:28 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLorainCountyBParser.java
M	cadpage-private

[33mcommit 8db36db5694c756d909c1a6891a1a5334172250b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 17:53:41 2018 -0700

    Parsing problem with Pierce County, WA (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit 52bbae08843b69d8135f6f973fd1683a608ce3f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 17:34:44 2018 -0700

    Added GPS Lookup table for Dane County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIDaneCountyParser.java
M	cadpage-private

[33mcommit 2ade14c8f5c6f0b09f6637bbb012df34dd7e8918[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 17:25:19 2018 -0700

    Fixed parsing problem with Madison County, IN (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMadisonCountyCParser.java
M	cadpage-private

[33mcommit 818a16edea6efaff06e0474aaf068f0fc6f04084[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 12:09:11 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit a4fc6f2ebf74c788731aa15fc08b69ed0f34f33f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 15 11:27:42 2018 -0700

    New Location: Solano COunty, CA (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA3Parser.java
M	cadpage-private

[33mcommit b18188178972928c061e81cbb31ba3425e28fa1f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Apr 15 01:26:51 2018 -0700

    general updates.

M	cadpage-private

[33mcommit dac8a8d66b3baa2544a535f34fe4973978f1e3e5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 13 16:24:16 2018 -0700

    New Location: McLennan County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMcLennanCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMcLennanCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMcLennanCountyParser.java
M	cadpage-private

[33mcommit 5f5447a6dc4a4449bc9dbebf795d1a6b8239f7fd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 13 10:03:40 2018 -0700

    New Locatoin: Milwaukee County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIMilwaukeeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWalworthCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA63Parser.java
M	cadpage-private

[33mcommit cef53056e5481c6ccc6c96a183718b7541f61fd8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Apr 13 02:45:08 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 56e5684b13f43a80b34fc1ef2f7eb3b5a4bad3f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 12 17:31:24 2018 -0700

    Release v1.9.15-37

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit cd995da37e8f971aaadea50916db92880905076a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 12 16:10:00 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit f3a788aa6f697ec3a4da8b023f21e5e245aee275[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 12 16:06:44 2018 -0700

    Fixed parsing problem with Jefferson County, CO (Add D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyParser.java
M	cadpage-private

[33mcommit 431beb51bbe2c621b9bea14b30e6f93467efa24b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 12 13:22:58 2018 -0700

    Fixed parsing problem with Bradley County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBradleyCountyParser.java
M	cadpage-private

[33mcommit d5ff20da2b7cb0b02fe02f970282c358042b5163[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 12 10:24:59 2018 -0700

    Fixed parsing problem with Jefferson County, CO (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyAParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit 671aeda0078c19e34505314b6f0511a4daa0608d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 11 17:20:14 2018 -0700

    Fixed parsing problem with Radford County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD4Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARadfordParser.java
M	cadpage-private

[33mcommit 3becba498ffdd23a5605c22f5b8a7bab5a4161f0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 11 13:30:57 2018 -0700

    Update sender filter for Loudon County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNLoudonCountyParser.java
M	cadpage-private

[33mcommit dbc39447ebe5b4aa853b3962f91931cbacabb515[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 11 13:09:13 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit fa330a0809a1c0e729efcac997f703bfaf9f1140[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 11 12:52:31 2018 -0700

    Fixed parsing problems with Botetourt County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABotetourtCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 3d9ee37bd1e3c0bc1b579d89870e66709331b96a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 11 11:33:46 2018 -0700

    Fixed parsing problem with Royse City, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRoyseCityParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit c276d295716070f132398c69e75471eedd0af78b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Apr 11 02:32:35 2018 -0700

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit 37c02d6f5ae8c519a21fe04bef26720650ea1084[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 10 13:25:18 2018 -0700

    Release v1.9.15-36

M	build.gradle
M	cadpage

[33mcommit 27dfdd3645115cfa5673c66d005d014e833a85aa[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Tue Apr 10 11:19:20 2018 -0700

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMcLennanCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIMilwaukeeCountyParser.java
M	cadpage-private

[33mcommit 2c1c551bacb5caf4243adce9263ec29cfa446494[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 10 10:29:10 2018 -0700

    Fixed more test issues

M	cadpage-private

[33mcommit 1eb881f170ca93a0edf60d1c3cee04bf359ab3ec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 10 09:36:44 2018 -0700

    Fixed A911 parser table problems

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 00ebb744645a5a18395110a9ef1b697beb144ad3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Apr 10 00:54:17 2018 -0700

    general updates.

M	cadpage-private

[33mcommit bf59f9642c2bd1b03c683209926a7f289c74055e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 9 09:00:40 2018 -0700

    Fixed test issues

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 73368a3b6fa3e012e20b09134ce49459575617a4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 8 17:50:07 2018 -0700

    New Location: Baker County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORBakerCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 8a1d7bb071903c92be9270183444849d88e3c7f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 8 14:58:14 2018 -0700

    New Location: Umatilla County, OR (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORUmatillaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORUmatillaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORUmatillaCountyParser.java
M	cadpage-private

[33mcommit 4693677fab1582736b6210db47c5571264dbecf3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 8 12:45:54 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit bebaab0cd1227ec4bf082111ca020ba4ebf4566c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 8 12:24:18 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit fd9b2a26b7a092c069837dc4de8c1c126d7435d4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 8 09:18:37 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit fdeb52a16ddc332fda9980e2e43e085d60fe31d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 8 08:59:19 2018 -0700

    Fixed parsing problem with San Luis Obispo County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyAParser.java
M	cadpage-private

[33mcommit b6b5a8b9fdbf9e35be17a21da134da29425070a3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 22:07:38 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 72c029260471006a66a5803873062781b5338ce4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 21:55:34 2018 -0700

    Updated GPS lookup table for Klamath County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORKlamathCountyParser.java
M	cadpage-private

[33mcommit 5e1f4eeb92af560f4d306dd8d597ed32dfe0afc9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 21:26:56 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 6ae019ec3e10709f501437f3762875d40459802a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 21:14:19 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit ad27fa598fded8af70669c1277b33772c2f396ba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 20:48:49 2018 -0700

    Updated msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD4Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-private

[33mcommit f80536911424f7c042b34a165b4dfae458fccc37[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 18:20:33 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 68880fb816e395d1f50274a294ab53f472d60577[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 17:51:31 2018 -0700

    New Location St Louis County, MO (I)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyIParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyParser.java
M	cadpage-private

[33mcommit f4643c482c89f6c81e440d7590b0e72a3d5a0db3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 7 11:55:20 2018 -0700

    Fixed parsing problem with DispatchSPKParser subclasses

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSPKParser.java
M	cadpage-private

[33mcommit 614860f32d0379033438f1b65ee1f5ee3734acae[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Apr 7 01:38:01 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 92115ea9db09332679afae233c72247b3f90ab87[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 6 22:58:45 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit c2b260a1a50f9ef6c60c35ff4d8ab03b2be5a8da[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 6 18:01:45 2018 -0700

    Fixed parsing problem with Northwest Public Safety, CT (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNorthwestPublicSafetyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNorthwestPublicSafetyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNorthwestPublicSafetyParser.java
M	cadpage-private

[33mcommit 94151bf22ec2517521e43c75de49a1b45b5d83ac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 6 17:17:54 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 91885c9dcaf4f37cdaf79deb7c19444c7c65f7a8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 6 11:23:58 2018 -0700

    Fixed parsing problem with Jefferson City, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJeffersonCityParser.java
M	cadpage-private

[33mcommit 117f84b04a96570da487bcf9b8e3f70629e97404[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 5 21:00:48 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 2aeba7d1e53f2e278d08e3bf7df67e8fcbc145ad[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 5 20:44:35 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 49198af929eda40550ccde49d8256dec37f88309[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 5 19:15:28 2018 -0700

    Parsing proble with Yolo County, CA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYoloCountyBParser.java
M	cadpage-private

[33mcommit b37fe612299f84921d81a7c23c5ca83c74681373[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 5 13:00:09 2018 -0700

    Fixed parsing problem witih Santa Cruz County, CA (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaCruzCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaCruzCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH03Parser.java
M	cadpage-private

[33mcommit a0ea3f6cf9afb3991b667cb00420e0dfc9b5b73f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 5 08:55:53 2018 -0700

    Release v1.9.15-35

M	build.gradle
M	cadpage

[33mcommit e05ef03d149d50189afb2b2b0bc7e66057a495f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 5 08:22:30 2018 -0700

    Fixed parsaing problem with Deschuttes County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORDeschutesCountyParser.java
M	cadpage-private

[33mcommit 4ea628c93e18cb43ab5ec83851f1da6ff17d6185[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 5 08:15:18 2018 -0700

    Fixed parsing problem with Boone County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBooneCountyParser.java
M	cadpage-private

[33mcommit f64ef635247116015fef0b1922f40375c0548186[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Apr 5 07:23:40 2018 -0700

    Update freeriders

M	cadpage-private

[33mcommit ef4cf94a062d1600a7f747adae336e1c9d610664[m
Merge: 4ff6b54 f1a72a6
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Apr 5 02:44:17 2018 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit f1a72a63aaac4b4ba8c54ece5e476a5dfa763afb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 4 20:48:20 2018 -0700

    Fixed parsing problem with Hoover, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALHooverParser.java
M	cadpage-private

[33mcommit 57f028ded743b0add6d541327e299a2483a328f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 4 19:20:53 2018 -0700

    Fixed minor parsing problem with  Chester COunty, PA (O)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-private

[33mcommit 364cb3967b37b55189fbd2536abec29e51c6a9b5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 4 19:12:22 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit f6a6f84e7d947b6feafd9ab48e7d6ca309f03f66[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 4 15:01:28 2018 -0700

    Fixed parsing problem with Polk County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAPolkCountyParser.java
M	cadpage-private

[33mcommit 0f84470d73a4cde7b3b3219c12e39cd82050fc98[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 4 11:03:33 2018 -0700

    Fixed parsing problem with Madison County, NY (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyBParser.java
M	cadpage-private

[33mcommit b7748b55f91aab781bc54252e569933c9e30aee8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 4 10:54:44 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit b7f3602866afa7887377575274b6e4a0fd68ba98[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 3 08:57:16 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 96ffc0c2d8e4ed64d43189d6d4d00f3377cc1e8a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 2 21:55:17 2018 -0700

    Update sender filter for Montgomery County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMontgomeryCountyParser.java
M	cadpage-private

[33mcommit e2ef322d30cf5fd4318d87176ced3ed14a3df919[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 2 21:44:02 2018 -0700

    Fixed parsing problem with Clarion County, PA (F)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyFParser.java
M	cadpage-private

[33mcommit 5dffb1963ce63483fcfee9a5cc36a08c8d6bf373[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 2 20:32:42 2018 -0700

    Fixed parsing problem with Minnehaha County, SD (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDMinnehahaCountyBParser.java
M	cadpage-private

[33mcommit 4ff6b5449a971f19de3b9fd3f19e28d837cf6aca[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Apr 2 13:55:50 2018 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit bf5d2d5d7bf0c0ed1c4d7b66c7a7764d00ce3ebd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 31 22:09:38 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 23cb1204fd9aa3f4ba4b503d0cabbb335245c3b5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 31 13:21:33 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit aad40cf9c56073581d46fc5870995e1ebe7d803e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 31 13:16:19 2018 -0700

    Fixed parsing problem with Hoover, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALHooverParser.java
M	cadpage-private

[33mcommit cd9959f6888a916c714b032fa2532f555bf25864[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 31 12:50:07 2018 -0700

    Fixed parsing problem with Macon County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMaconCountyParser.java
M	cadpage-private

[33mcommit 43127ecd3a62e61b4205173e3054c4e25ed0f1cf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 31 12:43:01 2018 -0700

    Fixed pafrsing problem witih Summit County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-private

[33mcommit 20b647274dc16851ba770516caffa66cddc13eb2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 30 19:51:20 2018 -0700

    Fixed parsing problem with Citrus County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCitrusCountyParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit bf8becc06cfec8894b7a1cfde597d248b8b603c5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Mar 30 02:34:17 2018 -0700

    general updates.

M	cadpage-private

[33mcommit e6d3c9b4a19e938b19bb6e07d3f5d85a867b5e9e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 29 19:53:37 2018 -0700

    Update A911 Parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALunenburgCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchDAPROParser.java
M	cadpage-private

[33mcommit ff6062629425e8e6581551eeecc268025a93f6a1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 29 19:20:51 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARockbridgeCountyParser.java
M	cadpage-private

[33mcommit 56cbe8cc96b4bf6506665d581abce87d301e5bf0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 29 18:58:52 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit e6f22c6d7c6b6e8521c7196b07c876018856e7b3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 29 18:39:56 2018 -0700

    Renamed Wharton, TX to Wharton County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWhartonCountyParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWhartonParser.java
M	cadpage-private

[33mcommit 4343258d174d426bdbe788dcc3a7d96dfff94ea7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 29 18:09:22 2018 -0700

    Fixed parsing problem with Licking County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLickingCountyParser.java
M	cadpage-private

[33mcommit 5ddbf53ad78b962b3341c6321d53048562fff76d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Mar 29 02:22:59 2018 -0700

    general updates.

M	cadpage-private

[33mcommit b2c8ba8b7904ec63224116fa002112f74f0e3c38[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 28 19:47:12 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSSumnerCountyParser.java
M	cadpage-private

[33mcommit 2086444aa1417a238714c6c0dcd666388be27659[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 27 15:29:53 2018 -0700

    Release v1.9.15-23

M	build.gradle
M	cadpage

[33mcommit 1d02b583246ab424dfa6b89052452962c08a5bf3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 27 14:56:54 2018 -0700

    Fixed parsing problem with Acadian Ambulance, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAAcadianAmbulanceParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/XXAcadianAmbulanceParser.java
M	cadpage-private

[33mcommit 7e0ef2a5d4f7eb0f44ca604ee059f5aa44ca1796[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 27 14:20:17 2018 -0700

    Fixed parsing problem with Chester County, PA (O)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-private

[33mcommit e145eab5396e7fda1e5f2557b0921c7d370a63af[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 26 21:27:37 2018 -0700

    Fixed parsing problem with Cleveland County, OK (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKClevelandCountyBParser.java
M	cadpage-private

[33mcommit e05c9a4771f5c687c97a1bc4a79a8eae4cc87780[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Mar 26 21:02:09 2018 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 8170cde0dc801b88639f72aef8b63cd5d6931bbc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 25 10:47:37 2018 -0700

    Release v1.9.15-33

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyDParser.java
M	cadpage-private

[33mcommit 1db7abdf3920a7319b6c25b9c96a55cc78768974[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 24 20:43:35 2018 -0700

    new Location: Grant County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIGrantCountyParser.java
M	cadpage-private

[33mcommit 2c9fb426b8e7aabd463ba967a5468523d619d766[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 21:26:37 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit fc99b186cbb780c29042f176bee48d6288b1c224[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 21:21:24 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyCreekCommCenterBParser.java
M	cadpage-private

[33mcommit d01bc8f2cdb76e64fd2b12750c7c0770ef616fc0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 20:56:28 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit d4b006845f8dd8f2286c0cf0c5822567a2b58f05[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 20:38:41 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit d84bf253d5bd94d1efb88a58b6e1d9a630bac3f2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 20:26:32 2018 -0700

    Fixed parsing problem with Sublette County, WY

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WY/WYSubletteCountyBParser.java
M	cadpage-private

[33mcommit f0c90a1dd06209ffea0f294bf4521017cf6f8725[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 16:53:27 2018 -0700

    Fixed parsing problem with Mecklenburg County, NC (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMecklenburgCountyAParser.java
M	cadpage-private

[33mcommit 6fce94e87755249ed9f3316f3816ccfed17711b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 16:29:10 2018 -0700

    Fixed parsing problem with Jefferson County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INJeffersonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit e099cee8108ebe0195dcb3c8956928d49af2f5f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 16:10:28 2018 -0700

    Parsing problem with Jefferson County, CO (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyCParser.java
M	cadpage-private

[33mcommit 16001e96a624330e152620e02a1e45e5eea0ca3b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 13:53:01 2018 -0700

    Fixed parsing problem with Acadian Ambulance, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBurnetCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/XXAcadianAmbulanceParser.java
M	cadpage-private

[33mcommit 9d9244e1d4e77256649afd9c46f2fe478d9f3ee1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 13:02:12 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit acf7abda4e97faea5602d6512e008d854d574e9e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 12:54:04 2018 -0700

    Sync everything

M	cadpage-private

[33mcommit 1e9834ab89bf0c15d36f0c5fc855bd430b8a90cc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 23 12:50:57 2018 -0700

    Fixed parsing problem with Muskingum County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMuskingumCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchCiscoParser.java
M	cadpage-private

[33mcommit 365e1edb3018ab69f3504bfb5f9e3b6a86e5861f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Mar 22 21:44:34 2018 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit e69b41719bd7b34e93487e51a6c36d756bc94503[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 22 19:51:13 2018 -0700

    Update genome.log

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 26faeafcdb5ccacd6e33debde7680a03110597d6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 22 19:07:03 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyAParser.java
M	cadpage-private

[33mcommit eaebe1eeab9c016d3e020f044e582a127e27e392[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 22 18:42:56 2018 -0700

    Fixed parsing problem with Columbia County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORColumbiaCountyParser.java
M	cadpage-private

[33mcommit 5ddc4e35176d9a91d7c3ca3fe7e14328e0143f8f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 22 12:40:49 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit d675abccc5bd9e814d6a36a9af919bd227cd0a4c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 22 11:34:22 2018 -0700

    Fixed parsing problem with Macon County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMaconCountyParser.java
M	cadpage-private

[33mcommit fa768c7f3294dc415d321484667b7622c9995123[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Mar 21 23:49:22 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 43458101a30baef660c5ea373258c6c58584b2fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 21 19:21:33 2018 -0700

    Fixed parsing problem with Cerro Gordo County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IACerroGordoCountyParser.java
M	cadpage-private

[33mcommit 34fff61ef0ec254132f9fdaf0b525789a2e3c985[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 21 18:45:20 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit e6ff20a1983da3562d562e84ae6b7d9a0afe75d7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 21 14:37:50 2018 -0700

    Release v1.9.15-32

M	build.gradle
M	cadpage

[33mcommit d49c260f6dff241d88fc12ef089ffff5f70ed863[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 21 11:14:59 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 11b3e99cbdb01a19e81c7c9e3aa2c08cd8e8d1ba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 21 11:13:25 2018 -0700

    GFixed parsing problem with Harvey County, KS (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSHarveyCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSHarveyCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSHarveyCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSPKParser.java
M	cadpage-private

[33mcommit 99082c7a0f199ceeb378e0bf0c23032a85868769[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 20 23:07:19 2018 -0700

    general updates.

M	cadpage-private

[33mcommit d042bf9f1d08a9e976723495eb2e18e65cbf8aac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 22:42:09 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 644c241c37a5f9b1ea74262c9fc4ba90fe972454[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 22:33:40 2018 -0700

    Fixed parsing problem with Chester County, PA (O)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA7BaseParser.java
M	cadpage-private

[33mcommit e9b3dfc8e2d289c878257b9567ef569b3e10ce32[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 21:28:12 2018 -0700

    Update call code table for Oakland County, MI (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyCParser.java
M	cadpage-private

[33mcommit 3c8a1f8ca5c09dff6dab43de00862b510c2383d6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 21:20:39 2018 -0700

    Sort call code table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyCParser.java
M	cadpage-private

[33mcommit 0c97dd169e7c2808b0d0c638f4a1eff733a26052[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 21:00:44 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNacogdochesCountyParser.java
M	cadpage-private

[33mcommit c5ae6efcebce91f33176e14cd7b7e88f90cf1536[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 20:44:40 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyBParser.java
M	cadpage-private

[33mcommit abc26a75ec2c81c74aefdf340fe4705630efa5f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 20:27:28 2018 -0700

    Fixed parsing problem with Nassau County, NY (N)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyNParser.java
M	cadpage-private

[33mcommit 98696890c7735314bcaf90af69282dd1108156aa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 20:02:51 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit b7f733af788b92ee5d5c42b80a58b7cfd74f9612[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 19:46:09 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 3816dcd2c9603289af783f587e6548719b131152[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 16:53:47 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 31bf51d76bdc7a683c4a8d6fd9e8f0487a5ed6c8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 16:50:49 2018 -0700

    Fixed parsing problem with Flathead County, MT (Add D)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyParser.java
M	cadpage-private

[33mcommit edab475d30efd78b401ffe4faac84ded84d3778e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 12:23:42 2018 -0700

    Ditto

M	cadpage-private

[33mcommit 84976bd70b23ed6ea72623f14d6f5551c963f28f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 20 12:23:03 2018 -0700

    Fixed parsing problem with Irecdell County,NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCIredellCountyBParser.java
M	cadpage-private

[33mcommit a366f83608aef86ed35d7a2b9346a54c1866e196[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Mar 19 23:17:47 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 358931c5bcb3f5418cc4a84b5a0f5f3529f1b470[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 19 20:48:15 2018 -0700

    Fixed problemwith Manatee COunty, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLManateeCountyParser.java
M	cadpage-private

[33mcommit f879718100aa70bf70db163bf41aa9c3bf4fd9bb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 19 20:17:40 2018 -0700

    Fixed parsing problem with Minnehaha County, SD (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDMinnehahaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDMinnehahaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDMinnehahaCountyParser.java
M	cadpage-private

[33mcommit 7d1d5517479e5b3dcb2cf0c3ac6ea53a328b0562[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 19 18:14:10 2018 -0700

    Fixed parsing problem with Roanoke County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCountyParser.java
M	cadpage-private

[33mcommit ae4d00884251ce456300788548a5d5badbe730a4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 19 09:45:41 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit 3822ed93b275f274da3a5ea8f8e8db14397b7b4c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 19 09:04:39 2018 -0700

    Fixed parsing problem with Chester County, PA (D4)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyBaseParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD4Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyParser.java
M	cadpage-private

[33mcommit df58fc7ec07cc5327a2e9b8a0c118c2b6af6e6e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 18 15:09:46 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 547b61f27c41c86747fae84cc9c28c1fd924951f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 18 14:41:52 2018 -0700

    Fixed parsing problem with Jefferson County, CO (Add C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyParser.java
M	cadpage-private

[33mcommit 3128f8711393cfcb0e63cc1774e6c004a9581741[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 18 10:41:48 2018 -0700

    Fixed parsing problem with Chester County, PA (O)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-private

[33mcommit 9d9815a97eafa23088cb3a4a294b4cc68db92ea6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 17 10:08:56 2018 -0700

    Release v1.9.15-31

M	cadpage

[33mcommit fb5ad4f821ec59ee3c1365b4abd5d683d0f5898f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 17 09:18:54 2018 -0700

    Fixed parsing problem with Roanoke County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCountyParser.java
M	cadpage-private

[33mcommit e2435af0b63e32f4eb9412ce981d5e2716a550f9[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 17 00:22:39 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 5bf579fe39f22041a7e1e5692fbeed76e602cf01[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 16 20:45:27 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java

[33mcommit db125b03b34d2983adde12a841c71726541114d6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 16 20:44:28 2018 -0700

    Fixed parsing problem with Jackson County, MO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJacksonCountyBParser.java
M	cadpage-private

[33mcommit 91fa50524974c226ff5316739566a0127b062ea4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 16 20:25:41 2018 -0700

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 85d8ac2a2c80821aaf99665d7f927668a054e16d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 16 17:27:04 2018 -0700

    Fixed repasing problem

M	build.gradle
M	cadpage

[33mcommit f7ea2b65b9740e2eed83f2c13038e138126422be[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 16 13:16:43 2018 -0700

    Fixed parsing problem with Benton County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORBentonCountyParser.java
M	cadpage-private

[33mcommit 25faea54ff95a80f6bcbebed50dcff3df31d6c3a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 16 08:49:17 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 7c50fc4397608564b3330a209bb8deafe176d871[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 15 22:01:11 2018 -0700

    Fixed problem with Allegheny County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-private

[33mcommit 7be1169711d07e685c1868d52c2abb01800904f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 15 21:34:20 2018 -0700

    Fixed parsing problem with Washington County, OR (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyParser.java
M	cadpage-private

[33mcommit 0915fb9efcf7e1fc1a814b1e0c2fd15aad97d47f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 15 17:35:31 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit 164e8d6bd1f9b5325949e4f8caa7ea31b3da1b2c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Mar 14 22:31:50 2018 -0700

    general updates.

M	cadpage-private

[33mcommit 1c10a6f49d48a9ce8879f813de0147bb697910d7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 21:02:33 2018 -0700

    Update sender filter for Douglas County, MN (A&B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDouglasCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDouglasCountyBParser.java
M	cadpage-private

[33mcommit 1a2fa4b18b8e9be19c8b16771c8480261a223801[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 20:57:49 2018 -0700

    Fixed parsing problem with Adams County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COAdamsCountyParser.java
M	cadpage-private

[33mcommit 4999e062f1c8d0f671ff5b8f3881b63f37202d74[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 19:55:10 2018 -0700

    misc update

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDPenningtonCountyParser.java
M	cadpage-private

[33mcommit a12cedbaba5486d331d5cd201f5ecc66321d7c04[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 18:40:44 2018 -0700

    Update genome.log

M	cadpage-private

[33mcommit adceb957e74ad5baab90689e7188dd730b918fe4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 18:38:35 2018 -0700

    Fixed minor problem with Madison County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMadisonCountyParser.java
M	cadpage-private

[33mcommit 642ff562f5452276452ec42358ad4f209d4bb304[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 18:22:53 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABCanmoreParser.java
M	cadpage-private

[33mcommit 2435b926ddf231cdca852994f794a74e619d9217[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 18:00:56 2018 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYStLawrenceCountyParser.java
M	cadpage-private

[33mcommit 43db1146626aab2f2718cd59491cff68a8247c11[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 14 17:46:20 2018 -0700

    Fixed parsing problem with Allegheny County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-private

[33mcommit b764fbbca247449227ac5c4a1846543c76ca4121[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 13 23:03:11 2018 -0700

    Update sender filter for Crawford County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACrawfordCountyParser.java
M	cadpage-private

[33mcommit 0023bd9bea62e1ee24ca0204d463ecb57054a99c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Mar 12 20:25:48 2018 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 1ad5f008a7d42dac5c6ac5d232f2a2fb19d9fa28[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 12 17:21:57 2018 -0700

    Checking in Knox County, NE test skeleton

M	cadpage-private

[33mcommit 53558e07e43430a500631a4a601df7d18a2d2812[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 12 16:10:54 2018 -0700

    Release v1.9.15-30

M	build.gradle
M	cadpage

[33mcommit 3091c06d913db8e6b318a75e8cf163be0026adcf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 12 15:24:11 2018 -0700

    Fixed parsing problems with Centre County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java
M	cadpage-private

[33mcommit 37041080d901c8ecd1dd1487f5f2b9214fa058cc[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Mar 12 13:25:05 2018 -0700

    general updates.

M	cadpage-private

[33mcommit cde1850addc7082beb26e5d3e9f40bd1c73e75ca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 12 13:05:49 2018 -0700

    Fixed DispatchA25Parser test classes

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA25Parser.java
M	cadpage-private

[33mcommit 9f2420ca80f3ca0718ea16f5ed53fd6aece63f0f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 11 18:17:05 2018 -0700

    Fixed problem with Huntingdon County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAHuntingdonCountyParser.java
M	cadpage-private

[33mcommit 9be9fc5653cc9a2a443c8021bedff3fe1a053de6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 11 18:05:06 2018 -0700

    Fixed parsing problem with Mendocino County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMendocinoCountyAParser.java
M	cadpage-private

[33mcommit 417255c4046ab15eb45a0ac125c55db4b60d9e2c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 11 16:37:28 2018 -0700

    Update msg doc

M	cadpage-private

[33mcommit c44b3afa36128463cd635a3dba2e714e5ae5862c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 11 16:05:39 2018 -0700

    Fixed parsing problem with Love County, OK

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA25Parser.java
M	cadpage-private

[33mcommit 2f5158d45a195bfbcb3d5c75797bf413a5cfc69f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 11 12:06:18 2018 -0700

    new Location: Huntingdon County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAHuntingdonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit da776d61083a571222a670e7f12e5729342f81a8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 11 11:34:38 2018 -0700

    Updated GPS lookup table for Murray County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMurrayCountyParser.java
M	cadpage-private

[33mcommit a4b932b369862470b4ba36b8085fac904b5ab289[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 10 20:58:54 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 0bf5429a0aeecfd22ac3c6a4a450ece773d228f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 10 17:06:07 2018 -0800

    Fixed parsing problem with Atlantic County, NJ (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticCountyBParser.java
M	cadpage-private

[33mcommit 27f68b5723beb41adb7b61370ec7237aba5dfb3b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 10 16:09:22 2018 -0800

    Update sender filter for Navajo County, AZ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZNavajoCountyAParser.java
M	cadpage-private

[33mcommit 6987e9b89b88dbe634152860f71184f874a913ee[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 10 13:34:38 2018 -0800

    Added GPSlookup table for St Lawrence County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYStLawrenceCountyParser.java
M	cadpage-private

[33mcommit 2a591f4cc69dcd26b3c0e1a3032c78587228e552[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 10 12:57:52 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 74f8f5ffcb52231913c4e00396f3d4b283534837[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 10 12:48:48 2018 -0800

    Fixed parsing problem with Oakland County, MI (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyCParser.java
M	cadpage-private

[33mcommit 043c4135c12ddc8b14edcc65e01a310a69ec6fbd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 10 01:11:36 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 6af711f92a945d186d7c96d8feafac396e7628b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 20:43:38 2018 -0800

    Fixed parsing problem with Canmore, AB

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABCanmoreParser.java
M	cadpage-private

[33mcommit 856099c418f85dd751a0f30be7d6eed250f75d35[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 20:06:50 2018 -0800

    Fixed parsing problem with Columbia County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORColumbiaCountyParser.java
M	cadpage-private

[33mcommit d8dc4de35933b40d544fd10075d4c9e8c450ea7d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 18:10:25 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit cfa1d4558b08e3e4bf560cef7b319b772b67c0e6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 16:45:34 2018 -0800

    release v19.15-29

M	build.gradle
M	cadpage

[33mcommit 175b93a5b749573d56363868f28fd50b764c16c4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 16:04:53 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 728beca0e6bea5ef8cb16ad04710da4ace50b773[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 14:30:09 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 474e2183b6028cd2b969e0f36221935426e17d2d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 14:05:14 2018 -0800

    Fixed parsing problem with Alamance County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAlamanceCountyParser.java
M	cadpage-private

[33mcommit 859c53f34967cb25b179205d1a9e4b781bfce385[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 12:29:26 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 185f764baff63ed5a14e08bd18e08ba8a968c9ca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 9 09:33:28 2018 -0800

    Fixed parsing problem with San Luis OBispo County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyAParser.java
M	cadpage-private

[33mcommit b97065eeed5860e80fb0b9bbb8aa1e33521232b8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 8 21:25:16 2018 -0800

    Fixed parsing problem with Juniata County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAJuniataCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA48Parser.java
M	cadpage-private

[33mcommit aa7e8c0539cbf33c86e4900c387d58ea617e8e7f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 8 19:58:42 2018 -0800

    Fixed parsing problem with Litchfield County, CT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyBParser.java
M	cadpage-private

[33mcommit 5d07edba6f9f6faef2e9eb48b7b865bf4caa8cc9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 8 19:39:52 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 92fd8b896c6bded2dcf56f700ebe33d01a3ea198[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 8 16:48:28 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 980d32a8f382c9355e7243a7694f6329f14e5124[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 8 16:31:16 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit ab15329da7cb4ff0dda600fa6544db30c9190a57[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 8 09:40:59 2018 -0800

    release v1.9.15-28

M	build.gradle
M	cadpage

[33mcommit 8f51c94bec7e89f7d5f083abd2817c1c87b43da6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 8 09:23:37 2018 -0800

    Fixed lots of parser problems

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARSebastianCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INLakeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDBaltimoreCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDGarrettCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYJeffersonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOntarioCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONMississaugaParser.java
M	cadpage-private

[33mcommit 3f28060bc634d1ccbcfc9c22511e643e7eae471a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 7 11:46:07 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 6ac8ec3973d847af797c73ce1e260c89ab8e03fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 7 10:15:07 2018 -0800

    Fixed parsing probem with Nassau County, NY (K)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyKParser.java
M	cadpage-private

[33mcommit 1c837e06ad39a65a877a6546b354e241d0719bdd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Mar 7 01:57:19 2018 -0800

    general updates.

M	build.gradle

[33mcommit bf3d1de0525d6367d5a689de23861317b24eda45[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 6 18:30:56 2018 -0800

    Fixed parsing problem with Madison County, NY (GLAS)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyGLASParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA13Parser.java
M	cadpage-private

[33mcommit 925e52d2d1bb4c8eb731754796c1c2fee5d309c9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 6 17:55:19 2018 -0800

    Clean up dead parser

D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyCParser.java
M	cadpage-private

[33mcommit 58a4ab367eb90d88271fd063163f06e6e27c1215[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 6 17:49:04 2018 -0800

    Fixed parsing problem with San Luis Obispo County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyCParser.java
M	cadpage-private

[33mcommit cf08339bab340b5e926a33ae9d842d520ef3dd26[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 6 02:06:50 2018 -0800

    general updates.;

M	cadpage-private

[33mcommit 29bd4cbea38bcf7a8b1a1d577ac2bb1d2d669648[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 6 01:39:24 2018 -0800

    general updates.

M	docs/support.txt

[33mcommit 3218750397a4ee8d4a57befd2e94284f0a08315c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 5 20:43:45 2018 -0800

    update genome.log

M	cadpage-private

[33mcommit 9e7f9de66a228f9c17715ae49d754886dea5bcd5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 5 20:39:48 2018 -0800

    New Location: Simcoe County, ON

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONSimcoeCountyParser.java
M	cadpage-private

[33mcommit 2520293be2aeca2e5729b6ba7024ddb6176276f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 5 14:42:58 2018 -0800

    Fixed parsing problem with Mississauga, ON

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONMississaugaParser.java
M	cadpage-private

[33mcommit 3b9aed21899e2963c6cad003fba75b2620810859[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 4 15:52:04 2018 -0800

    Fixed parsing problem with York County, PA (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyDParser.java
M	cadpage-private

[33mcommit 3210d5225420ec31e018cd8e169904be960aba2e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 4 10:47:49 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit be1db0cb9b25d572f41a9bcc2e3d905d231108f0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 4 10:40:06 2018 -0800

    Update sender filter for DesMoines County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IADesMoinesCountyParser.java
M	cadpage-private

[33mcommit 69785c17d95426c50a56f99b1b491486471a2970[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 4 10:33:17 2018 -0800

    Fixed parsing problem with Chatham County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAChathamCountyParser.java
M	cadpage-private

[33mcommit a8f967d5e3ef8b5101b6fc2e61a358c9463371fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 17:09:54 2018 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTGallatinCountyBParser.java
M	cadpage-private

[33mcommit 6d33dd8be597857bc3f3da9a8bf1d12c0deae7c4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 16:24:18 2018 -0800

    Fixed parsing problem with Clarion County, PA (F)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyFParser.java
M	cadpage-private

[33mcommit 4bfb372e8293c922122a4e1a56b8c6fcd21a5bc1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 14:17:40 2018 -0800

    Fixed parsing problem with Bowling Green, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBowlingGreenParser.java
M	cadpage-private

[33mcommit fdf6f8f87d18b49ff148cc997b6e43ff49ecc45e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 13:58:04 2018 -0800

    Update GPS table fro Marion County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit 5525ad10c0607a1cd6a44b46a42720596b169798[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 13:46:05 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 50d6663d18c4a0086d4d7715ff588cf13940eadf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 13:23:27 2018 -0800

    Release v1.9.15-27

M	build.gradle
M	cadpage

[33mcommit f05b1978bca7be2ca8d5276cb63f3bedcdb250af[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 12:24:37 2018 -0800

    Update everything

M	cadpage-private

[33mcommit ff40990cfd0589ac43d873d7de8fcc809b3371f2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 3 12:22:41 2018 -0800

    Checkign in stuff

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyCParser.java
M	cadpage-private

[33mcommit 5b4761bef7d6707a8cf1b568453e6cbc20edb3e8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 3 01:33:06 2018 -0800

    general updates.

M	cadpage-private

[33mcommit f21a5877ff022f09c2872789fa5ebd5f06fdc852[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 2 21:07:21 2018 -0800

    Update sender filter for Buffalo, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBuffaloParser.java
M	cadpage-private

[33mcommit 0b8ea69b4de8df84d7da55458efc7f5303ec081a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 2 18:42:38 2018 -0800

    Fixed parsing problem with Charleston County, SC (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyAParser.java
M	cadpage-private

[33mcommit a77a72d5bab6550e9aa1ec9304bf7f9c8702ccc3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 2 09:31:34 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 814ec285894f4a2e60f47544150f7ecf65b10c98[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 2 09:20:40 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit b52d864dc972af2e654f7e4c81e6e0e30dbad2e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 1 20:58:33 2018 -0800

    Fixed parsing poblem with Litchfield County,CT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyBParser.java
M	cadpage-private

[33mcommit 714b27df76eda27fc306f8b4591af89bf395cad5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 1 19:30:27 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 330d9d94413ea34922be0ee5adce32fce7d47295[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 1 19:18:53 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyFParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyJParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALehighCountyAParser.java
M	cadpage-private

[33mcommit c33388e643af2437fa4e291c238e25fc2a61538f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 1 16:39:56 2018 -0800

    New Location Middlesex County, CT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTMiddlesexCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTMiddlesexCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTMiddlesexCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchRedAlertParser.java
M	cadpage-private

[33mcommit 5b8362e71537da9e73afa0b896596fa65dff1a93[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 1 16:01:13 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 8cb304b6c102a6839da61acc9d017efc438c8df4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 1 15:53:26 2018 -0800

    Upgrade to Gradle 4.1

M	build.gradle
M	cadpage
M	gradle/wrapper/gradle-wrapper.properties

[33mcommit 883ac5038d4ca3fa2a60e105cf0b95352ecd60c5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Mar 1 00:03:30 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 6633ba010d2b88513786fd507104c94f8d3e3ef9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 21:29:04 2018 -0800

    New Location: Bergen County, NJ (F)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyParser.java
M	cadpage-private

[33mcommit f9840b04db2829222d7f8e7c6c23add68fce2262[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 21:01:19 2018 -0800

    New Location: Bergen County, NJ (G)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyParser.java
M	cadpage-private

[33mcommit 16693e54671aabea3e7b9692594d8c1a786da7fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 20:51:54 2018 -0800

    Checking in Patrick County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPatrickCountyParser.java
M	cadpage-private

[33mcommit b74073c6f0861d6d3a51d838a1965c284165d2a4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 20:14:34 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLakeCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLakeCountyParser.java
M	cadpage-private

[33mcommit 87c611f4efdc492301b0db6d768b209dcdf2dfb1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 19:38:12 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 618fc52bf549f02833d85c184b40997fbc52dea4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 19:15:30 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-private

[33mcommit e926b7ca534479763e063dc009c0b55c85e67111[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 18:03:01 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 61d8580c0e349d747549d6a9d2c06b3459dab65e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 17:58:41 2018 -0800

    Fixed misc stuff

M	cadpage-private

[33mcommit 3a3ea504419ab09b2154b69fa7dc6cc1a7a748de[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 16:59:18 2018 -0800

    Fixed parse test class

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStarkeCountyParser.java
M	cadpage-private

[33mcommit e7ab30b6e9a806ebb8b7df82a4a53ca141d9c11c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 28 07:45:46 2018 -0800

    Fixed force close in exception handling logic

M	cadpage

[33mcommit 401dec363a240640131f1ebdc905a9671e336893[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 27 21:36:34 2018 -0800

    Update genome.log

M	cadpage-private
D	hs_err_pid18623.log

[33mcommit e541c037442f4e314debde777c2cbd488bf05668[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Tue Feb 27 11:49:29 2018 -0800

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTMiddlesexCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyJParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPatrickCountyParser.java
M	cadpage-private

[33mcommit 7a7a4d4cf231d36fced86dbcb6545d4bc025de10[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Feb 27 02:39:46 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 2c5648948cefb2c7607458fe4abc3575b11d1e92[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 26 17:31:55 2018 -0800

    Release v1.9.15-26

M	build.gradle
M	cadpage

[33mcommit bb44ac2611a6e916afd4796612dde76934c73874[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 26 16:40:48 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 428e8ef66fe2ec0538af02c156304c702fd07bb9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 26 14:25:26 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 324d5ed1687e001dbd70ae014e0c6f83d3872dec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 26 12:57:10 2018 -0800

    Fixed parsing problem with Erie County, NY (Add G)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyGParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyParser.java
M	cadpage-private

[33mcommit 303c23c23cb90f05c4d10cfb1336fd7b8124d4f7[m
Merge: 6b5f281 1bb35b0
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Feb 26 09:19:31 2018 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 1bb35b0291a94d196b2280b6fd3a685956d2d78f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 26 08:07:53 2018 -0800

    Clened up unit reporting for DispatchA29Parser subclasses

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA29Parser.java
M	cadpage-private

[33mcommit 6b5f28132099d9d67954ca2891c252b6ffc09344[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Feb 26 02:25:39 2018 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 8fdbcbdc73dc015d3a16ffc3795b9fa9c02982ff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 25 18:35:21 2018 -0800

    Fixed test problems

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA29Parser.java
M	cadpage-private

[33mcommit 177d8324e4bbb25a94739f379425b6ac07760aaf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 25 17:43:31 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 501401d7ece32674b05e6971a99ca2fe6658df1a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 25 17:40:02 2018 -0800

    Fixed parasing problem with Pottawatomie County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA25Parser.java
M	cadpage-private

[33mcommit 7da4be6551b3b2e5c725a65efeff5809e4570d75[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 25 17:23:09 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCIredellCountyBParser.java
M	cadpage-private

[33mcommit 734a766cb37caaf9cdaf9b12bd907afb7badd719[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 25 16:56:31 2018 -0800

    Fixed parsing problem with Starke County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStarkeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA29Parser.java
M	cadpage-private

[33mcommit 47303c38ee13d913aa561b0fdd77ad6a6d0115b9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 24 17:20:48 2018 -0800

    Release 1.9.15-25

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishAParser.java

[33mcommit a74d53635a96643eaece05a8a86492b2b5cee91c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 24 15:11:14 2018 -0800

    Fixed parsing problem with Greenville County, SC (Added B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGreenvilleCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGreenvilleCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGreenvilleCountyParser.java
M	cadpage-private

[33mcommit 3b9f16ad127fe20859d3afd1f084fdd24c954fdd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 24 10:11:33 2018 -0800

    New Location: Terrebonne Parish, LA (B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishParser.java
M	cadpage-private

[33mcommit 84db4a007461539c02330685163110d9782121ac[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Feb 24 01:39:49 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 0344023117d0e3d7199ca4e242ec3887c226a469[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 23 13:59:55 2018 -0800

    Update tables for Terrebonne Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishParser.java
M	cadpage-private

[33mcommit 104a01dddd143a72e1811301322554e24a81457c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 22 18:56:51 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 922c66c342e943872f782e373b0414fe9e5bda23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 22 11:39:56 2018 -0800

    Fixed problem with group block parsing accepting general alerts

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GroupBestParser.java

[33mcommit 5b712d9626a3c6ad1b9323a47df7cd7a2b4f9d30[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 22 10:16:06 2018 -0800

    Release v19.15-24

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java

[33mcommit 277857c1f13d4ff4a3e0c54c8b98a2c5353cb196[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 21:01:15 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 022fa6c2b35ade620606fe37c07fab470f3b9703[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 20:36:18 2018 -0800

    Checking in Gillespie County, MT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGillespieCountyParser.java
M	cadpage-private

[33mcommit 9a1377021fd91e6344217d85e48ed1ce0522b056[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 20:14:25 2018 -0800

    Checking in stuff

M	cadpage-private

[33mcommit 2337a3aca4cd387297a3f8b82fb57fec7ef50fde[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 19:34:27 2018 -0800

    Checking in San Miguel County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COSanMiguelCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTLamoilleCountyParser.java
M	cadpage-private

[33mcommit b8b0808534948bd5bc43766aabf54a316551e87e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 18:56:33 2018 -0800

    Checking in Orleans County, VT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTOrleansCountyParser.java
M	cadpage-private

[33mcommit db226d86ad2693e9de7abf13a4d9bacbbed3b358[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 18:46:30 2018 -0800

    Checkign in Bourbon County, KY (B) and Burnet County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBurnetCountyParser.java
M	cadpage-private

[33mcommit 59b21d3631153dbcfcbc3fa128101607dcbf1ace[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 17:48:50 2018 -0800

    Checking in Noble County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit b061b77bdb8250bf876adf29a11395adce3b7b1e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 17:39:18 2018 -0800

    Integrated Dallas County, TX (D&E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyParser.java
M	cadpage-private

[33mcommit a1db02fca3fbeed03361f0b0f065c03d04b051a8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 16:56:50 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit dae63fdc8790b59c59f3b7d59609d118673ae6d6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 16:46:00 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 77fa524b1531d2c39df7342c9b381ae2eb7c3442[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 16:36:12 2018 -0800

    Update sender filter for Fort Knox, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYFortKnoxParser.java
M	cadpage-private

[33mcommit ddbfd6de3968ed2fc79728845ed3a39eb6ccafe7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 16:26:05 2018 -0800

    Fixed parsing problem with Dane County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIDaneCountyParser.java
M	cadpage-private

[33mcommit fd0deba90b90c12fe08a55959533442dc294dbc2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 15:24:30 2018 -0800

    Update sender filter for Fort Bnd County, TX (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFortBendCountyAParser.java
M	cadpage-private

[33mcommit 1855869203406016f42651b971cd57181d3d1da6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 15:15:25 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 013dc5e38bc6e8731ba58124ae3d960326ae30a2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 15:06:38 2018 -0800

    Update call code table fro San Bernardino County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyAParser.java
M	cadpage-private

[33mcommit fe353b8181ca54cc7401044f00c974590474c232[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 14:48:36 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyCreekCommCenterAParser.java
M	cadpage-private

[33mcommit 8e72bb7f6f500d4ab64163864b322422b14babad[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 13:20:18 2018 -0800

    Fixed parsing problem with Anne Arundel County,MD (Added EMS2)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyEMS2Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyParser.java
M	cadpage-private

[33mcommit 4bae8891f6517c77340918df9330b1e719e8ee4b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 21 11:55:29 2018 -0800

    UKpdate A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 51095875443584e9a3ad2f4e1121bdde290e7888[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Feb 21 00:39:07 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 774317d4de9aedd95d68563c6181b25b92ebe1fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 20 21:08:18 2018 -0800

    New Location Greenbrier County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVGreenbrierCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABTaberParser.java
M	cadpage-private

[33mcommit 22a8e2465789c34d85f8115070864b519e1cf1b6[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Tue Feb 20 13:57:42 2018 -0800

    skeletons

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHNobleCountyParser.java
M	cadpage-private

[33mcommit c3efb718e3e70481dff954cdb2d52a9436a7cc6f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Feb 20 01:22:09 2018 -0800

    general updates.

M	docs/support.txt

[33mcommit d0e6a5a8367adb0b3e3353b67f2899704e20a0f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 19 20:51:42 2018 -0800

    Checking in Floyd County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFloydCountyParser.java
M	cadpage-private

[33mcommit 9658341f0b22b4be7ab0d572ce601e602bfbf5bd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 19 19:46:30 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyParser.java
M	cadpage-private

[33mcommit fd5191ed2907c4c2fc1e0a9f0a9efb33c224927b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 19 19:26:35 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGravesCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYStatePoliceParser.java
M	cadpage-private

[33mcommit 67eb0cba0f4d521a1ec839bd9ad20eaa63902af6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 19 16:45:50 2018 -0800

    Checking in Etowah County, AL (A) & Butler County, KS (C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALEtowahCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALEtowahCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSButlerCountyParser.java
M	cadpage-private

[33mcommit 7f3333a32f4561b8f4f236b8c6a86d329658f95e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 19 15:59:10 2018 -0800

    Cleaned up problems iwth DispatchA27Parser based parsers

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYChristianCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHGraftonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA27Parser.java
M	cadpage-private

[33mcommit 607cb358eaa7c496bccf1c969aa6e461d60ac051[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Feb 18 02:13:59 2018 -0800

    general updates.

M	cadpage-private

[33mcommit dba69658a09dfe89af5c08869eee3d0d7afad607[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 17 23:41:59 2018 -0800

    Checking in Allen County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSAllenCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 47bcaa5f4915e51803ce391ed5c0ac23c7abe83a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 17 23:22:45 2018 -0800

    Checking in Stephens County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit af25836e3b3782ccc51cf11052d3ea93bbde1161[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 17 23:11:37 2018 -0800

    Checking in Wayne County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 4954b1baf361fc30371b2d1139fe849cedc2bbec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 17 22:59:12 2018 -0800

    Checkign in Carroll County, NH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHCarrollCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHGraftonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 7b4df81b3a314a29bd60226b51dc67ea9a3df777[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 17 18:14:09 2018 -0800

    Fixed parsing problem with Chester County (Add P)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyPParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyParser.java
M	cadpage-private

[33mcommit 7b2af206f66e5a3f80f49b3ba6a653de0b5696b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 17 11:20:05 2018 -0800

    release v1.9.15-23

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyParser.java
M	cadpage-private

[33mcommit 5fb83ec3dc7b75375a9fab3ce6e95f4883086341[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 17 08:34:39 2018 -0800

    Fixed more parsing problems with Gloucester County, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJGloucesterCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchProphoenixParser.java
M	cadpage-private

[33mcommit 87f474d23bced038d423343cdf5f339fdf160aa6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 17:16:06 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyAParser.java
M	cadpage-private

[33mcommit ec99588ed438cd0de1d7e72f60b5c0593ab0b465[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 16:42:15 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 488de840d44bceb513f5dad17edf317c5a5d7966[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 16:19:08 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 570cdbee575afa345fe397fdd017e96e166b369c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 15:36:53 2018 -0800

    Cleanup Halifax County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHalifaxCountyParser.java
M	cadpage-private

[33mcommit a704da2d306d7efd46adb6d25a3ce30ad7d92376[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 14:51:36 2018 -0800

    Updated call table fro Kern County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAKernCountyParser.java
M	cadpage-private

[33mcommit 8329e361292e4b5445d9f6319be99af2d2749d4e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 14:30:16 2018 -0800

    Fixed minor issues with Delaware County, PA (B&G)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyGParser.java
M	cadpage-private

[33mcommit d3cda9aef70e42fbb6daffd3493a2f7c43aa4dd2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 11:29:26 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 1eb1a083d58516ac9c4f1821f2c2579c2517fbaa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 16 11:14:30 2018 -0800

    Fixed parsing problem with Bay County, MI (Added C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBayCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBayCountyParser.java
M	cadpage-private

[33mcommit d41fa180f96a043bc5e72851cd9eeca0a2af569e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Feb 16 01:44:08 2018 -0800

    general updates.

M	cadpage-private

[33mcommit f89d68b25a1a3a7dcb50df14b878498beb2b2002[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 15 21:15:09 2018 -0800

    Fixed parsing problem with Clackamas County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyAParser.java
M	cadpage-private

[33mcommit b0ae7ed779b20ef6882612b5f5fb45e4eea8ca84[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 15 20:00:52 2018 -0800

    Fixed parsing problem with Halifax County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHalifaxCountyParser.java
M	cadpage-private

[33mcommit 189d6de2a533c8cee9282cb478e5c719a844d708[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 15 16:58:27 2018 -0800

    Fixed parsing problem with Brunswick County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBrunswickCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 3a949d604ad7b9050e3df7d0fc046a4f1a865770[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Feb 15 14:37:30 2018 -0800

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALEtowahCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSAllenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSButlerCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGravesCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyEParser.java
M	cadpage-private

[33mcommit 5145da2a25de7ba84b0b0efbce2f62f69c7b1e76[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 15 13:14:31 2018 -0800

    Fixed parsing problem with Gloucester COunty, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJGloucesterCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJGloucesterCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchProphoenixParser.java
M	cadpage-private

[33mcommit 7f01aa585b23822d14b41278eb695fe47286127f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 15 10:01:57 2018 -0800

    Fixed parsing problems with Saginaw County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MISaginawCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH03Parser.java
M	cadpage-private

[33mcommit 7ad5f04bddd1a122af11dfbb6a615e7ed7db89ca[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Feb 15 01:03:25 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 7ee35d3aa07add992c6fa1af7334476e702a77e6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 21:51:32 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit e8a6020d32cec2ea0b5de6b9d5047e690d064ce1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 21:34:38 2018 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MISaginawCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH03Parser.java
M	cadpage-private

[33mcommit 85fb50dd982fcbe062f25ba323ae4243493214f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 20:19:33 2018 -0800

    Fixed parsing problem with Tulsa, OK (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaParser.java
M	cadpage-private

[33mcommit c909453c54e60a75eaf6f2f2daff2cbefc34069b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 20:01:09 2018 -0800

    Fixed parsing problem with Colquitt County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAColquittCountyParser.java
M	cadpage-private

[33mcommit ec93e1d4a0e5f231e836d058ba0222d41cd8f3f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 19:13:00 2018 -0800

    Fixed parsing problem with WOrth County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAMitchellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAMuscatineCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWorthCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWinchesterParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA47Parser.java
M	cadpage-private

[33mcommit ffe31858d6fef5abbf4f51a31f4db9cec0ab370f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 14:38:58 2018 -0800

    Fixed parsing problem with Crawford County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACrawfordCountyParser.java
M	cadpage-private

[33mcommit 94c9d7b8160550207344c8229ffa17c47e03fe18[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 14:02:00 2018 -0800

    Fixed parsing problem with Clarion County, PA (Add E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyAParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyBParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyFParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyParser.java
M	cadpage-private

[33mcommit 4cb465aca81b52ec39ae949c731bf6f86e27f66b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 14 08:42:43 2018 -0800

    Release v1.9.15-22

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPolkCountyParser.java

[33mcommit 620a2a493d3e5d78ad9003e2c9a0bd22f548d174[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 13 19:01:45 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 7b62ccca28cbc1525b6524dd88df7144cd6e1186[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Feb 12 23:41:57 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 45ff9ffdeb7540ac7496ec89901e6c51b5a97328[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 12 18:24:23 2018 -0800

    Fixed parsing probelm with Groton, CT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTGrotonParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyCParser.java
M	cadpage-private

[33mcommit 2eca5a8da9d7fbb9b089e71e961965da93618fca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 12 17:25:14 2018 -0800

    Fixed parsing problem with Blount County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBlountCountyParser.java
M	cadpage-private

[33mcommit 6c58aa31d91efcda5a8816089660dea8bdc0be54[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 12 13:47:09 2018 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPolkCountyParser.java
M	cadpage-private

[33mcommit 53f750a339ab15f48214d65556259996305f6c8d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 12 13:38:13 2018 -0800

    Fixed parsing problem with Polk County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPolkCountyParser.java
M	cadpage-private

[33mcommit 2bbb72ca1137783d284e698e351962fdc7967beb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 12 12:05:51 2018 -0800

    Fixed parsin gproblem with Washington County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWashingtonCountyParser.java
M	cadpage-private

[33mcommit 214188e2e1fc9880368604cd2ac271debe78848c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Feb 12 01:09:03 2018 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 13d221665e42ed0e3dcad2951abca4430b16aec2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 11 19:48:49 2018 -0800

    Fixed parsing problem with Muskingum County, OH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMuskingumCountyBParser.java
M	cadpage-private

[33mcommit dd9ff8a412c06287ef92cddf4806d93b25823509[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 11 08:32:34 2018 -0800

    Relesae v1.9.15-21

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 66965db44ad4415b971aecff76a2fc02ad06c75d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 10 21:16:43 2018 -0800

    Fixed parsing problems with San Joaquin County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanJoaquinCountyParser.java
M	cadpage-private

[33mcommit 0dcbf18fa964003c2b176a763a0a567fd327872b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 10 18:10:31 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit c79b52fc278702299bb1ff74d3b061405fec750a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 10 17:56:13 2018 -0800

    Fixed parsing problem with Cass County, ND

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
M	cadpage-private

[33mcommit 1fc22e4fe071c3e52e023a847ba4ddfb40acd551[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 10 12:16:35 2018 -0800

    Fixed parsing problem with Fort Knox, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYFortKnoxParser.java
M	cadpage-private

[33mcommit f9468432dcb539359326a25ba9959e1af4ed1e99[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 10 11:00:49 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 818db5d333c11bfb96da48566abd8c2ff52cff48[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 10 08:41:53 2018 -0800

    Fixed parsing problem with Morris County, NJ (Add D)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyParser.java
M	cadpage-private

[33mcommit 8b8dd579085096a8bdc262dbb7dcd6ac19ce1add[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 10 07:35:15 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 64316e1f62587532409be36a229be6320314c733[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Feb 10 02:46:03 2018 -0800

    general updates.

M	cadpage-private

[33mcommit b92427327038638e2bc2a1b1e625225e3c7540a7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 9 20:41:56 2018 -0800

    Update sender filter for Cuyahoga County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABCanmoreParser.java
M	cadpage-private

[33mcommit 464b0adfec93e53b19b662a2eea28df3e4f4e021[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 9 20:24:44 2018 -0800

    Fixed parsing problem with Perry County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPerryCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit eaf1aee042f6f4e138b1441da7566f958e21d411[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 9 12:07:03 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 20de2ab5794f80157cdba35ddcb54a2b1ded7bf9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 9 11:13:19 2018 -0800

    Update call code table for Tangipahoa Parish, LA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishAParser.java
M	cadpage-private

[33mcommit dd3605c3efe273e352fa313e9257350aad0f9bbc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 8 21:11:44 2018 -0800

    Fixed parsing problem with Crawford COunty, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACrawfordCountyParser.java
M	cadpage-private

[33mcommit 3f1c85f38264c749e16cd1120cf7512f2ab90f0d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 8 19:40:43 2018 -0800

    Fixed parsing problems with Bay County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBayCountyAParser.java
M	cadpage-private

[33mcommit cc299c9b4184cf0e150acf1c0967aa329892f4c1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 8 17:31:04 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WICalumetCountyAParser.java
M	cadpage-private

[33mcommit 14986dd2c33400d860d0a49e0967cd2019219c7b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 8 16:42:07 2018 -0800

    Fixed parsing problem with Craven County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHavelockParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 70bd97af1d2883126a54964dfd25470d58d78e6f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 7 14:50:11 2018 -0800

    Release v1.9.15-20

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-private

[33mcommit f7c153ed7a40f596fa7b771ae7599a8a4ef8ea86[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Feb 7 14:27:36 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 6c1693e7cdd529ab33acc2b799d09d87be089821[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 7 12:12:12 2018 -0800

    Sync everything

M	cadpage-private

[33mcommit d26c38712d3e6b4e659b363d7e0aabb739498f82[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 7 12:03:14 2018 -0800

    Fixed parsing problem with Somerset County, PA (Add C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-private

[33mcommit 5d109b28bd410e90af13301768a2b28c117cc93c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Feb 7 02:00:55 2018 -0800

    general updates.

M	cadpage-private

[33mcommit c417029f18e00bd702cb36d9e2cdb6edb0e5a48e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 6 21:20:57 2018 -0800

    Fixed parsing problem with Jackson County, MO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJacksonCountyBParser.java
M	cadpage-private

[33mcommit f69f09414eba4f6c98984be583f318c0affe8547[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 6 16:59:38 2018 -0800

    Fixed parsing problem with Flathead County, MT (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH04Parser.java
M	cadpage-private

[33mcommit 3bb8f32ae427568ee6b925903ed291563d6320a2[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Feb 6 15:07:30 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 3d759b186cdd4a2718a897a47acf4244ae51cf2c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 6 14:59:36 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 66125fa493ab22764d10909cf05ee5e8b691ee54[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 6 14:27:23 2018 -0800

    Fixed parsing problem with Campbell County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCampbellCountyParser.java
M	cadpage-private

[33mcommit 7b36c18168054403eb1870e3a5b1e5624c2ee7b3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Feb 5 22:39:54 2018 -0800

    general updates.

M	docs/support.txt

[33mcommit ee58d88162fb9f7388c5188772064c16b2535661[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 5 09:18:08 2018 -0800

    Release v1.9.15-19

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit de314317e8729f8d5c00490038cb2283bd8b9113[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 4 15:11:27 2018 -0800

    Fix tests

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyAParser.java
M	cadpage-private

[33mcommit 7a6304fe04734aa702a84221e7513cc7b1e863c2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 4 13:55:01 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit e73ade1c13f6d568d5c7d1e7a3ebf5f93bd85aa5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 4 13:54:36 2018 -0800

    Fixed parsing problem with Cascade County,MT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyBParser.java
M	cadpage-private

[33mcommit 2959b34cc35f06723712bc22b4a08eadc624c97c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 4 13:22:28 2018 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBerrienCountyParser.java

[33mcommit 19ac14484c39b397e79a7c15f6dc7ea58c99f76a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 4 11:44:43 2018 -0800

    Fixed parsing problem with errien County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBerrienCountyParser.java
M	cadpage-private

[33mcommit c89a630834d05f6c4ed4efce09a16ef5bd73cb54[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 4 10:33:38 2018 -0800

    New Location: Miami-Dade County, FL (B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLMiamiDadeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLMiamiDadeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLMiamiDadeCountyParser.java
M	cadpage-private

[33mcommit 9d9a08a81fa6de256660e1e57e931c8f50a13308[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Feb 4 01:49:02 2018 -0800

    general updates.

M	cadpage-private

[33mcommit c1134ba0757624dd32ffda8dfc62657e9c42409d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 3 12:07:47 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit d94f80832423312b50a536d90b7372488ef32aa5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 3 12:01:06 2018 -0800

    Fixed parsing problem with Clark County, OH (Add D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit d39edf714275ba44fdedb00dc7560ebc959c9c9b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Feb 3 01:10:01 2018 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 3d19d63ed6b67f1f40e2bf2df10ebe56a1ec1065[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 2 16:43:59 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 43f9d32b16e2165f9ee4ce2d15ad3ba06e184483[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 2 15:20:14 2018 -0800

    Fixed parsing problem with Fayette County, PA (add C skip B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyParser.java
M	cadpage-private

[33mcommit b0f0a0af06ddb4c34fbaf27fe5219448b8ed8646[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Feb 2 03:32:07 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 85ed9c7239f0127a66ab804c6ba99b97daec08b5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 1 22:52:29 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit f62c72dd51961f944cf095d5de2b53314c4d69d3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 1 22:19:48 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit b9997e4558963dc31265d882a4b92f19ef7fae56[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 1 22:09:46 2018 -0800

    Fixed parsing problem with Flathead COunty, MT (Add C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRockwallCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH04Parser.java
M	cadpage-private

[33mcommit 5b5704cd925ce0ee29a9f85efb867da7249f5095[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Feb 1 00:08:57 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 84c6f726531b414b2b384c20b57035689b996ef9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 31 11:02:27 2018 -0800

    Release v1.9.15-18

M	build.gradle
M	cadpage

[33mcommit 3762a56058726b06b24a9e81b98195e2deca2dfb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 30 23:24:14 2018 -0800

    general updates.

M	cadpage-private

[33mcommit abbb70910bf41c1ca7b03027f333edabce6f9c93[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 30 16:59:20 2018 -0800

    Fixed parsing problem with Humbold County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAHumboldtCountyParser.java
M	cadpage-private

[33mcommit 6a2d9e6066cbd5c1f23cac8eba8459fbca8aa3be[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 30 16:12:22 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 6e617d49db32eca9b181a341ee0104fbb62386f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 30 16:07:36 2018 -0800

    Fixed parsing problem with Oldham County, KY (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYOldhamCountyAParser.java
M	cadpage-private

[33mcommit c452034e4e7e6556318a88c4e530a0ddec1c7210[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Tue Jan 30 15:43:00 2018 -0800

    parsers and skeletons

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOWayneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHCarrollCountyParser.java
M	cadpage-private

[33mcommit c48474668f1d8f65a64ecc70eca0f11be0a8c5e8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 30 15:08:35 2018 -0800

    Fixed parsing problem with Vermillion County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INVermillionCountyParser.java
M	cadpage-private

[33mcommit 69d7d547d8274f8e231047e70bd918892d70f12b[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Tue Jan 30 14:06:18 2018 -0800

    skeletons

M	cadpage-private

[33mcommit 8511b89b43a647f5f49a33012965980cd2dcb13a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 30 06:59:58 2018 -0800

    Update sender filter for Wichita, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSWichitaParser.java
M	cadpage-private

[33mcommit 95f6ef4264d4dfc36016c6762d99c828dbc348aa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 30 06:53:29 2018 -0800

    Fixed parsing problem with Mecklenburg County, NC (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMecklenburgCountyAParser.java
M	cadpage-private

[33mcommit 3c677fe45ab6275c7d69c9615b1dfcbb916e1a7c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 30 01:08:12 2018 -0800

    general updates,

M	cadpage-private
M	docs/support.txt

[33mcommit 4c092f8edefa56c1dbbde5ad1a70d503dedd3d9e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 22:59:04 2018 -0800

    Fixed parsing problem with Butler County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAButlerCountyParser.java
M	cadpage-private

[33mcommit 3bd6dc5b65b2201052cb785a4b6052c3538a1c23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 21:34:49 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 0d5a28810dc0c53e2745acb732ac22ef22563c39[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 21:20:19 2018 -0800

    Parsing problem with Nassau County, NY (N)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyNParser.java
M	cadpage-private

[33mcommit b746364f4b36cc80ebd5c1917908509ec209d57d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 20:29:04 2018 -0800

    Fixed parsing problem with Boone County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBooneCountyParser.java
M	cadpage-private

[33mcommit 278da93023163f1ddf3f45b88c734170efbdf5a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 19:09:05 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 4e3a6454b2748185539ca0b4212c60911453e913[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 18:42:19 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 2fc9d71d3598214f64f241d8fcbc9fb2e432f7fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 12:56:56 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit e39474392c0d221e6ec1ed7a222cdcd116ec59cd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 12:53:48 2018 -0800

    Update sender filter for Hancock County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILHancockCountyParser.java
M	cadpage-private

[33mcommit d1f77b36862d71e2143bffadda5834a2e872e269[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 12:29:59 2018 -0800

    Fixed parsing problem with Kane County, IL (Added D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyParser.java
M	cadpage-private

[33mcommit 6d8fdef7aeaf8093a0f03d2f5b4e3491fd3e4ff5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 29 08:20:19 2018 -0800

    Release v1.9.15-17

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNashCountyParser.java
M	cadpage-private

[33mcommit 3f53225fecfb3d5029ed9a3f42354c9e43c22c4d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 28 21:32:24 2018 -0800

    New Location: Winston County, AL

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALWinstonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
M	cadpage-private

[33mcommit daec6a1c05a41d4804edc9d4f2b5281bd4cb7c5d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 28 19:29:35 2018 -0800

    Parsing problem with Walker County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWalkerCountyParser.java
M	cadpage-private

[33mcommit 35b4bc17309f8e55876adf02a0ddcb68774985e3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 28 12:53:30 2018 -0800

    Fixed parsing problem with Nash County, NC (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNashCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNashCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNashCountyParser.java
M	cadpage-private

[33mcommit e3d30560d3578330951eff0a9410ad9d6b97068d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 28 10:18:37 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit d203e24b9a22b9f924d94dd9345a61dc4c156fd4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 27 16:58:42 2018 -0800

    Fixed problem with GPS lookup table in Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 64b2147308a6e3ce539d5d1b1f458413510aad44[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 27 13:32:52 2018 -0800

    Fixed parsing problem with Kankakee County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKankakeeCountyParser.java
M	cadpage-private

[33mcommit de539f6d5afe2ae1635bb83b2938154e9300a56d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 26 20:56:57 2018 -0800

    Fixed parsing problem with Linn County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit f58856eb5b3bb81e9d15c2d8098d7657f6668d52[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 26 19:19:52 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit fe88ac620f64eced0d0782fd3ca737b22dbffcc9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 26 19:09:43 2018 -0800

    Fixed parsing problem with Teller County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COTellerCountyParser.java
M	cadpage-private

[33mcommit de0f3e16dab3a884b77935178aedf8e73efa0ede[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 26 18:16:07 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit fb48c6729e19bb00c43ea10cb71fc83bd0c305fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 21:18:11 2018 -0800

    Fixed parsing problem with Cascade County, MT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyBParser.java
M	cadpage-private

[33mcommit b895328adb8500912acb416a3c34f259fed9b830[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 19:16:02 2018 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA25Parser.java
M	cadpage-private

[33mcommit 46f003cb1b1be738521532df28efe03cf3c4c202[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 18:26:17 2018 -0800

    Release v1.9.15-16

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java

[33mcommit 6ed84ce224047eaf4ae97783c302d42a9f1418f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 15:57:57 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit fce97aa2bd8a8628941d8773e730e42d75bfc749[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 15:24:32 2018 -0800

    Fixed parsing problem with Tehama County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATehamaCountyParser.java
M	cadpage-private

[33mcommit 362eb749537d9009c68be05fb65c493e6aaac0b8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 15:02:12 2018 -0800

    New Location Warren County, OH (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWarrenCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWarrenCountyParser.java
M	cadpage-private

[33mcommit 86b5dedd21d4e2ae509d108bf7224cae7f49c633[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 12:57:10 2018 -0800

    Fixed parsing problem with Macon County,NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyParser.java
M	cadpage-private

[33mcommit 7f4143d345e2d98ef9a78552f11c2d895969aa32[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 25 12:38:52 2018 -0800

    Additional fixes

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHColumbianaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 7589503b396b52deae472e47a98537241f318275[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jan 25 00:56:27 2018 -0800

    general updates.

M	cadpage-private

[33mcommit eb4b6c36ef34e0d66a2cd8de8bdb2931d98c4d2b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 24 21:00:10 2018 -0800

    Fixed parsing problem with Mahoning COunty, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 97d432c7ad1dec8cb3dc4cddf96e46128869419d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 24 17:00:38 2018 -0800

    update msg doc

M	cadpage-private

[33mcommit 06ab04046c03af55580a725ef6e65571f0de9761[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 24 16:30:43 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit af366c977f25f16e6ad26fe4c09537eeaf75065e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 24 11:38:23 2018 -0800

    Added logic to catch exception in donation menu logic

M	cadpage
M	cadpage-private

[33mcommit 4a766b3a1b3f628502b951469d5eea920f7e90e2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 24 11:01:34 2018 -0800

    Fixed parsing problem with Butler County, KS (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
M	cadpage-private

[33mcommit 4659b40a4ec1368b4092a5c65288200076e59ce6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Jan 24 01:43:20 2018 -0800

    general updates.

M	cadpage-private

[33mcommit f5c8ea35e702b1fd9094e9da506321be998b8e1f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 23 20:22:39 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 15d173a74580e26071df64688547a0543cf8d1f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 23 14:13:05 2018 -0800

    Fixed parsing problem with Burke County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBurkeCountyParser.java
M	cadpage-private

[33mcommit 306093484216dd77b44d246eac8ae8b08b7741aa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 23 14:03:10 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 5e3f9af54db5eac206af55048fc0cc4eaa91e514[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 23 02:19:08 2018 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit c68d6da850206e15f16c86fb26aa63629f9d1916[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 22 22:56:13 2018 -0800

    New Locatoin: Tehama County, CA

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATehamaCountyParser.java
M	cadpage-private

[33mcommit aba05758133acab25236185af7d55f8dde72753f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 22 12:08:15 2018 -0800

    Release v1.9.15-15

M	build.gradle
M	cadpage

[33mcommit daef0f186d6ddeaac18054f41cd4cc6cc2576c35[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 22 11:16:49 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit f2591416e21cca56125319c0f76d34e2e9e200bf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 22 10:23:10 2018 -0800

    Fixed test failures

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLJacksonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit 93c7847da3a7c9cd4043096323129c4e8fbed287[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 21 20:45:46 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 3e4c342604cedb06c9527cf97a908e68231646d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 21 20:26:53 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 630f2c413624b0b7300881a52bf3f216dd28ddc7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 21 19:03:01 2018 -0800

    Fixed parsing problem with Ashland County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyAParser.java
M	cadpage-private

[33mcommit a1fdec032970a2335fcbe95bd62814f4d2b49eb8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 21 15:09:33 2018 -0800

    New Location: Ford County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSFordCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 5da19b608caa7d74e8676ac508547263f2925123[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 21 14:50:04 2018 -0800

    Fixed parsing problem with Columbia County, PA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAColumbiaCountyBParser.java
M	cadpage-private

[33mcommit 1af2742b69a8c1e64bc0ad148be4a713d98e85d8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 21 11:16:59 2018 -0800

    Checking in partial results

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAColumbiaCountyBParser.java
M	cadpage-private

[33mcommit 3f454b0fe4cd3b72e27558d24a2b79352fadf48b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jan 20 17:42:45 2018 -0800

    general updates.

M	cadpage-private

[33mcommit a9f2a4ba3bdd8db64d7b982d0fca5aeea988e3a2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 19 19:50:19 2018 -0800

    Fixed sender filter for Monroe County, pA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMonroeCountyParser.java
M	cadpage-private

[33mcommit 15843bdb575ce501ea4063898bb1139972c7cf65[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 19 19:34:27 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit b833b28a8295c266c75178dec50af977ca89c975[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jan 19 19:18:42 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 1cb7ae0a8abceb5a299c0fd20d7e722443ea324b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 19 19:11:24 2018 -0800

    Added Holms County and Jackson County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java

[33mcommit 0724bc4fc9656bbc5cff75c767515614766d3a53[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 19 19:07:46 2018 -0800

    New Location: Holmes County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLBayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLHolmesCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLJacksonCountyParser.java
M	cadpage-private

[33mcommit cf7a1ecd019d01cf204ad73ff425b344083bb9e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 19 11:48:22 2018 -0800

    New Location: Hart County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAHartCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit d3e764bd759dd00cb296abe96704c491a7192aed[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 19 11:19:09 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 469f6029cdc38b94e8c0216aabd15883caf16c59[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 18 23:10:12 2018 -0800

    Added Travis County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CodeTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTravisCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTravisCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTravisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZipStateTable.java
M	cadpage-private

[33mcommit 5b7b154f1d1a36603bf3b76eaf928dd53059d82e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jan 18 21:15:46 2018 -0800

    general updates.

M	cadpage-private

[33mcommit a897ba12b427f239d981a9d47dac5b6f13e31218[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 17 22:34:21 2018 -0800

    Fixed parsing problem with Pitt COuty, NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILClintonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBlountCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchOSSIParser.java
M	cadpage-private

[33mcommit 39290b1f5ec99412288f927981fd159e98e1ac9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 17 17:34:47 2018 -0800

    Update GPS talbe for Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgInfo.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit c17d33e1eb7d91626fbf34247c3a225cc1df7570[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 17 11:43:53 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARockinghamCountyParser.java
M	cadpage-private

[33mcommit f75e05401431ecdb0b93b70f8f9e4fa342a995c4[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Jan 17 02:22:47 2018 -0800

    general updates.

M	cadpage-private

[33mcommit c53ed37cb1e144c10167a4b243a6e25fc2163bdf[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 16 16:54:41 2018 -0800

    general updates.

M	cadpage-private

[33mcommit b00f324891343cdc39bdf9763d5da0872ee609fc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 16 13:52:40 2018 -0800

    Release v1.9.15-14

M	build.gradle
M	cadpage

[33mcommit 93db633c03d0b0f4ed80dd19897a3ff392d19ec7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 16 13:35:13 2018 -0800

    Fixed backward compatibility problem with parser updates

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java

[33mcommit 0a0babc499ba6887b1749ef51f18df31ebda499f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 16 13:04:57 2018 -0800

    Fixed parsing problem with Madison County, NY (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyBParser.java
M	cadpage-private

[33mcommit 52d0122075fae7c5c3bfba3a7c56b9f80fdd373a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 16 12:26:51 2018 -0800

    Fixed parsing problem with Saginaw County, MI (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MISaginawCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MISaginawCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MISaginawCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyC2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH03Parser.java
M	cadpage-private

[33mcommit f3b567fd089b3f47d678f44a5b53425958045e86[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 16 08:18:32 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit b29ebb2741ed705ac26909de91c2b1c516063a76[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 15 21:40:32 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit e999953570cfe2c13ced26090ced201a918bb1e0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 15 21:28:05 2018 -0800

    Fixed parsing problem with Boulder County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyBParser.java
M	cadpage-private

[33mcommit 3ad4c5b5aac51c805a6fb2186390d6807240d3d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 15 19:59:41 2018 -0800

    update msg doc

M	cadpage-private

[33mcommit c84f79cb508ff0b06e8493188f5580da00b48d97[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 15 18:46:18 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit c8b07d2445d58909499e722c2ca897fcc43d182e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 15 18:42:55 2018 -0800

    Fixed parsing problem with Carroll County, MD (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyCParser.java
M	cadpage-private

[33mcommit dd32d3b6be09d00a2a97b4ee4b84ca874f003be8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 15 14:47:00 2018 -0800

    Fixed parsing problem with Madison County, NY(B) and Oneida County,  NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOneidaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA13Parser.java
M	cadpage-private

[33mcommit 9a7c3c8705ad6a932d60405dc503a6f105397c0b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 14 15:05:18 2018 -0800

    Release v1.9.15-13

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 0278ee1b4b3760a303d968f3ec89bf0919e2ec24[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 14 12:10:53 2018 -0800

    Added new location: Carroll County, MD (C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyParser.java
M	cadpage-private

[33mcommit 54363a22ddfe4e85b90735e0b983c705d4178d22[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 14 11:40:09 2018 -0800

    New Location: Denver, CO

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODenverParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit b4c6644f7e1d8400b088bbee441615dcf6ba8135[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 14 11:06:37 2018 -0800

    New Location - Monroe County, MS

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSMonroeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit e2ab27f1016dd6f95ca74280e9d56253f111b4f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 13 16:21:32 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit 6fbf721e9745271a988bdd2b6af2a6f347631d84[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 13 16:15:03 2018 -0800

    Fixed parsing problem with Macon County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyParser.java
M	cadpage-private

[33mcommit b75f97b98e42b3d4e908725f795ecf6d4c3d091a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 13 14:07:50 2018 -0800

    Update GPS table for Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 5aa49122867327b884b6049ac2f7544322a137a3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 13 14:02:41 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit b3f071680f8f0c807b9f6773f98142114d2a7eb1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 13 13:11:36 2018 -0800

    Update sender filter for Franklin County, VA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinCountyBParser.java
M	cadpage-private

[33mcommit fe614caf3e71924ca3d6efdf39ae5949d742672f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 13 12:38:46 2018 -0800

    Fixed parsing problem with Clinton County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA29Parser.java
M	cadpage-private

[33mcommit 77feb99c3505f19b2af7c97708e3241c37e87dd0[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Fri Jan 12 14:08:56 2018 -0800

    skeletons

M	cadpage-private

[33mcommit 20aafc22decc878fb7eaa0f0139d4f5184337b69[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 22:55:14 2018 -0800

    Fixed parsing problem with Palm Beach County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLPalmBeachCountyParser.java
M	cadpage-private

[33mcommit 03bc8a2090e1efe9e05b6d04547668d71f5804c0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 22:36:08 2018 -0800

    Fixed parsing problem with Christian County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChristianCountyParser.java
M	cadpage-private

[33mcommit eecda34955e63bd46eabdd73389199ebb8c25dde[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 22:31:32 2018 -0800

    Fixed parsing problem with Licking County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLickingCountyParser.java
M	cadpage-private

[33mcommit fb9122781dd61aabab97bfe58c9907452790bcf3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 22:20:05 2018 -0800

    UKpdate sender filter for Wayne County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWayneCountyCParser.java
M	cadpage-private

[33mcommit 5db4e7e4f036006df6e89dbf686d2b26875febfb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 22:16:07 2018 -0800

    Ditto

M	cadpage-private

[33mcommit e3a9e454ddf85fc010b56843e3f307ae42d63d20[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 21:45:11 2018 -0800

    Fixed parsing problem with Washington County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASnyderCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWashingtonCountyParser.java
M	cadpage-private

[33mcommit 288cac7a96f1ae8f95db0d611021dad9f601e5c0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 17:12:49 2018 -0800

    Fixed parsing problems with Snyder County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASnyderCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB3Parser.java
M	cadpage-private

[33mcommit 954a5b8ca26c2d56c09e81797e882b605020ce17[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 11 09:49:07 2018 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBrazoriaCountyBParser.java
M	cadpage-private

[33mcommit 07b095b8ac277901a6982011aa3b335c74926774[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jan 11 04:12:02 2018 -0800

    general updates.

M	cadpage-private

[33mcommit 1458f9e0137f96bb424dd23438b364b4ff7a41f0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 10 21:41:26 2018 -0800

    Update sender filter for Mesa County, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMesaCountyBParser.java
M	cadpage-private

[33mcommit 9305dd7f30c8029593cbe064fb99ca4ed1b97c1b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 10 21:35:36 2018 -0800

    Fixed parsing problem with El Paso County, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COElPasoCountyBParser.java
M	cadpage-private

[33mcommit c14bfba874e37e4d7c6b13acc269180f8adae270[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 10 21:02:08 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit d2b22084678e84d5c8d8c9e91ce6feab4428e076[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 10 20:53:38 2018 -0800

    Fixed parsing problem with Henrico County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHenricoCountyParser.java
M	cadpage-private

[33mcommit fbac8acab20c197d35c6f0e8e7a990232b125b77[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 10 18:24:09 2018 -0800

    Fixed parsing problem with Chester County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCChesterCountyParser.java
M	cadpage-private

[33mcommit b671cd6944ca1ef37c69aa592958c04a57756eee[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 10 14:59:02 2018 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit c6d577e75e8719a833c424c21dfc08dcaa590b6a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 10 08:31:43 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 414665cda6839dc0dc5109e112cae7cb552cb531[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 9 22:15:19 2018 -0800

    Release v1.9.15-12

M	build.gradle
M	cadpage

[33mcommit 65fb3924fe5ad6c9d39d08cb8cac1d4e61effe24[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 7 12:33:40 2018 -0800

    Fixed parsing problem with Garrett County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDGarrettCountyParser.java
M	cadpage-private

[33mcommit beb278bd7e9a69d6e7d4aa1a492cca8175005912[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 7 11:50:49 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 330ae3786f9fe3fee911f32d30680045d8ce0af5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 7 11:41:53 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit 5e4358305b9def49c3dccfe466546e234c851d68[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 7 11:11:48 2018 -0800

    Fixed parsing problem with Chambers County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALChambersCountyParser.java
M	cadpage-private

[33mcommit 2da899122530055333abb428dc0c791a101836a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 6 15:49:19 2018 -0800

    Fixed parsing problem with Hudson, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHudsonParser.java
M	cadpage-private

[33mcommit fc42b63889ae2d90428e3e8f913eb6fe36b1d9c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 6 13:21:06 2018 -0800

    Update Active911 account exception lit
    Update msg doc

M	cadpage
M	cadpage-private

[33mcommit 293e9a133626e9a221a557b01606504844f61a9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 6 12:48:01 2018 -0800

    Update genome.log

M	cadpage-private

[33mcommit f5f690698dc3c892566c2bc032853de514c2d919[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jan 6 02:39:18 2018 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit bb0ad1d480954da868f4682b94f77ab099e0feb8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 5 12:58:57 2018 -0800

    Fixed misspelled city entry

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABrunswickCountyParser.java
M	cadpage-private

[33mcommit 0c76302b8b5e93a507bfcfbef2046c2cc8f03732[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 5 12:50:31 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit ad2b9700d99984f65e921450d2ac88c29446cf58[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 5 12:07:22 2018 -0800

    Update sender filter for Josephine County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORJosephineCountyParser.java
M	cadpage-private

[33mcommit a15e7266f3ba38d091bed914548a6fb7f2ac4a43[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 5 11:20:11 2018 -0800

    Release v1.9.15-11

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 4c5ac217f51476dbaf07caeaeb39ef84206b61f6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 5 10:17:39 2018 -0800

    Fixed paring problems with Ventura County, CA & Chester County, PA (D4)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAVenturaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD4Parser.java
M	cadpage-private

[33mcommit 6445e9c30a78ed51fc653619df1cb9f9f99a4999[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 3 22:07:35 2018 -0800

    Reversed gradle update

M	build.gradle
M	cadpage-private
M	gradle/wrapper/gradle-wrapper.properties

[33mcommit dc3d032a5e38af1859e659c267505d9d19d7d44b[m
Merge: 98242dc caa727e
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 3 19:52:51 2018 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit caa727e7cc63a919a4ddd5d48dfe70b10a1bc912[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Jan 3 04:47:05 2018 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit a87dd33c79318777495bb07a638ca1f410536a70[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 2 20:12:53 2018 -0800

    Update msg doc

M	cadpage-private

[33mcommit e3bde202c82cdb68e68cff7106c979abf0099459[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 2 19:51:19 2018 -0800

    Update call table for Emanuel County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 98242dca667393ce8a507355c7a7b1c7397bbb43[m
Merge: ac07abf 42dbf95
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 2 14:57:54 2018 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit ac07abf8d9168d9e93cb2d75bc5327048c4eea0e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 2 14:57:33 2018 -0800

    Update gradle config

M	build.gradle
M	cadpage
M	cadpage-private
M	gradle/wrapper/gradle-wrapper.properties

[33mcommit 42dbf9533cffcb905a6aaebce565689dff0c8fe8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 2 09:26:50 2018 -0800

    Release v1.9.15-10

M	build.gradle
M	cadpage

[33mcommit be7a4ade924b1307f8d11e998955749243a07d26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 1 15:49:32 2018 -0800

    Fixed parsing problem with Chester County, PA (D4)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD4Parser.java
M	cadpage-private

[33mcommit 861df3dc07844e18aa37de32a0afbbc729f9fc81[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 1 11:50:25 2018 -0800

    Fixed startup performance issues

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java

[33mcommit f4aa7fde60a68075513be61cffefb168c582b614[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 31 15:15:34 2017 -0800

    Release v1.9.15-09

M	build.gradle
M	cadpage

[33mcommit 203e93d1b6be35b75c758b9ce802e5745e1d854a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 31 10:11:57 2017 -0800

    Fixed some test issues

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyBaseParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyNParser.java
M	cadpage-private

[33mcommit c882361867c23fd85207a11085b378e09de76ee8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 31 00:35:51 2017 -0800

    Fixed parsing problem wtiih Suffolk County, NY (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyBParser.java
M	cadpage-private

[33mcommit c0f9793b027ce858a72d53e5c956adeb74579882[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 31 00:13:30 2017 -0800

    Fixed anothe rproblem with Pittsylvania County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPittsylvaniaCountyParser.java
M	cadpage-private

[33mcommit 016843507ec90ad3d68565ce1c1b81c6521684d9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 20:49:52 2017 -0800

    Fixed parsing problem with Danville, VA and Pittsylvania County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADanvilleParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPittsylvaniaCountyParser.java
M	cadpage-private

[33mcommit fc555d5d391ce597ce338b26ed48cdcd4fd5835d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 19:12:07 2017 -0800

    Fixed parsing problem with Danville, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADanvilleParser.java
M	cadpage-private

[33mcommit 32c95db1a7109a773428a255e014c1753ce7049b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 14:35:48 2017 -0800

    Fixed parsing problem with Yukon, OK

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKYukonParser.java
M	cadpage-private

[33mcommit 540f5e6e11e7c4b90869e3244519ed4795990c81[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 14:07:15 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit cf18a882b7c2ce2adf6ec41b006c42d290acdb51[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 13:52:12 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICassCountyParser.java
M	cadpage-private

[33mcommit 29934dcec4a6a69ac9c2690b4a43fd38b26d60f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 13:41:48 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit fd100ae53c107935ce23425bb528ee92432a2d6b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 13:40:06 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f15970b0990731eb236059b0c1f7f75b0a0eabc3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 30 10:36:12 2017 -0800

    Fixed parsing problem with Chester County, PA (N)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyNParser.java
M	cadpage-private

[33mcommit 3774e4b6192092a7a6e40104904a8274418de5d7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 29 20:34:54 2017 -0800

    Updated GPS table for Saint Marys County, MD

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSaintMarysCountyParser.java
M	cadpage-private

[33mcommit 3a336546cb364377f592b07244eb231e133951dc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 29 01:47:52 2017 -0800

    Release v1.9.15-08

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit b37160021c13f0e74495df73a4d587f7e642940e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 28 23:33:27 2017 -0800

    Fixed parsing problem with DuPage County, IL (A&D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyAParser.java
M	cadpage-private

[33mcommit 8e55a6b0ff1e4940396bd6b4c59396520a75d98a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 28 22:00:14 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 8d3ab8969f96fe37d03aaaef761fc82c9b04a070[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 28 21:47:57 2017 -0800

    Fixed parsing problem with Jefferson County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJeffersonCountyParser.java
M	cadpage-private

[33mcommit 9f350a50e70d7f6e1cd08d5334e3e7c07e7afce2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 28 19:48:13 2017 -0800

    Fixed parasing problem with Clarion County, PA (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyEParser.java
M	cadpage-private

[33mcommit 909ab8e4a804ea5584aa1857237bbbf1d66ae2fa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 28 17:53:17 2017 -0800

    Checking in Jefferson County, AL (K)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyKParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyParser.java
M	cadpage-private

[33mcommit 6aa64509901f8b31622465fee574124f16e1baf9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 28 17:34:53 2017 -0800

    Fixed parsing problem with St Mary's County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSaintMarysCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGalvestonCountyParser.java
M	cadpage-private

[33mcommit 248badb4d10256e4b83cf700f39b1e81a2f267e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 28 14:58:05 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 46c8c4ac55af90798892cee1fa2ca7d2265474a7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 27 20:44:54 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 7b9631f75d4cdec131126439088face2f52a4555[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 27 15:05:09 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 9962b21e3645e1cdf2c4bcb3aa0daed17a42ecf6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 26 21:24:47 2017 -0800

    Checking in Page County, VA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPageCountyBParser.java
M	cadpage-private

[33mcommit 5d5a2865ff7f938fdeb3ec1112c65015356a7502[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 26 15:16:50 2017 -0800

    Checking in Miami County, OH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMiamiCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMiamiCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMiamiCountyParser.java
M	cadpage-private

[33mcommit df11e65d1f1f1f1874251ba1099c8fc093d1748f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 26 15:08:35 2017 -0800

    Checking in Stephens County, GA & Rowen County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAStephensCountyParser.java
M	cadpage-private

[33mcommit ef1e642183e8610340b55bf942eea65022c8f1d8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 26 14:30:32 2017 -0800

    Checking in Page County, VA (B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPageCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPageCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPageCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 1993159b1e53e43aec7e57673412a9183c832e84[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 26 13:15:30 2017 -0800

    Checking in Ellis County, TX (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODouglasCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYStatePoliceParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyHParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORJosephineCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyCreekCommCenterAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXEllisCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXEllisCountyParser.java
M	cadpage-private

[33mcommit bff45278e76c7c4fcf1dde6094715ef57b4f912b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 24 13:29:10 2017 -0800

    Fixed parsing problems with Craven County, NC (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-private

[33mcommit 75c9410c35e3af0825f41a4704bf3295ab99817d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Dec 24 00:21:22 2017 -0800

    general updates.

M	cadpage-private

[33mcommit e2821bff083369fc2f0910ab9d7ec1bf3ad501a1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 23 14:48:04 2017 -0800

    Release v1.9.15-07

M	build.gradle
M	cadpage

[33mcommit 29c63e0f67e2c69fc06acca4df077d869c31796d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 23 14:20:48 2017 -0800

    Update genome.log

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLouisvilleParser.java
M	cadpage-private

[33mcommit 20fb31ab8575bf4aa34b39321fc2163cda535062[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 23 14:17:51 2017 -0800

    Fixed parsing problem with Cumberland County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCumberlandCountyParser.java
M	cadpage-private

[33mcommit eb42a0d4ef2a5606036d76f640c7baf80bbed637[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 23 11:23:55 2017 -0800

    Update sender filter for Morgan County, WV (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMorganCountyBParser.java
M	cadpage-private

[33mcommit dcf754f9b1182030a06a7f3f010803737e40c852[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 23 11:12:26 2017 -0800

    Fixed parsing problelm with Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit 6797305cec32b957b91b7bff5f24edaa7dccc26c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 23 08:37:15 2017 -0800

    Fixed parsing problem with Orangeburg County, SC (Add C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOrangeburgCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOrangeburgCountyParser.java
M	cadpage-private

[33mcommit acf5b716b3bfae334d576f05dfa91f8da0ae2c3e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 22 22:09:22 2017 -0800

    Fixed parsing problem with Butler County, KS (Add B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSButlerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSButlerCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSButlerCountyParser.java
M	cadpage-private

[33mcommit 401f0302182c2e59e5194bf8b4444825d6f7dcfc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 22 21:01:29 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 67445c136c718b8e142ff58fa221eb152e8b0785[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 22 12:51:30 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 8eb3effa59759387bb0319fff32b62658ba7b433[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 22 12:48:45 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 1c9ebd73c191620bec61624e8ba29f96a2310985[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 22 12:07:06 2017 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABrunswickCountyParser.java
M	cadpage-private

[33mcommit 60ddbe632e77d685abc78b2f87304b2f136ced02[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 22 11:43:58 2017 -0800

    Fixed parsing problem with Brunswick County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABrunswickCountyParser.java
M	cadpage-private

[33mcommit ff70355a0957d607e13640952f890d3a14d0343e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 21 20:27:20 2017 -0800

    Added GPS  mapping table to Sublette County, WY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WY/WYSubletteCountyParser.java
M	cadpage-private

[33mcommit 54024eee6948620052f2735ef17b256c28e274db[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 21 12:53:59 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 1da3ec5bd9937d62f4b119af8002aad3ac3680d2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 21 11:30:14 2017 -0800

    Fixed parsing problem with Cass County, ND

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-private

[33mcommit 3e2ec100e7d8400371b3987ba4165739c8fd6a74[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Dec 21 03:22:29 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 965385dac39f0e0006a9ea158387ff10787654d0[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Dec 19 03:41:09 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 2adb083b3580043b5fe6975b2f4dd55fafc45e76[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 18 21:55:48 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit dc605c4b26099466a0e70ba9bfaf33213d51456a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 18 20:21:45 2017 -0800

    Update sender filter for Howell County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOHowellCountyParser.java

[33mcommit d68d6db242f152502cba8a7e58533717e452b6da[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 18 19:52:39 2017 -0800

    Update sender filter for Manatee County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLManateeCountyParser.java

[33mcommit 07a18d0635c3609892e9e0113da84205ec84556b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 18 13:34:06 2017 -0800

    Fixed parsing problem with Eden Prairie, MN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNEdenPrairieParser.java
M	cadpage-private

[33mcommit e5fe20b1abab6ef1e42ddff1134607116839f77a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 21:03:45 2017 -0800

    Updated city table for Prince Edward County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPrinceEdwardCountyParser.java
M	cadpage-private

[33mcommit 379f2b98e394ddd945d324fac2d5c031dfffdb06[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 20:59:25 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 81b1818ab3bf1780360e3ec5a99322a3dbe45b70[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 18:22:31 2017 -0800

    Fixed parsing problem with Atlantic County, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticCountyAParser.java
M	cadpage-private

[33mcommit c50e036ac4e71c3b5c8113dc88b1b2589b32e2a0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 18:14:33 2017 -0800

    Fixed parsing problem with Duplin County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDuplinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 59003100a3912db7b68b92cad939da55ac43edc2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 15:59:49 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 038079bcd6b822bb8186a058b8171c6fe18512e9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 12:38:12 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f2108d1112dfa5d74a1795db646e7a51aad8b9dd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 10:21:28 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 16c0ef894cc4cea02824997fe889558932ff0b57[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 17 10:05:51 2017 -0800

    Fixed parsing problem with Halifax County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHalifaxCountyParser.java
M	cadpage-private

[33mcommit ea6d19117fc2a5b6b86d8912955cc7499c22f414[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 16 10:33:54 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyGParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchGlobalDispatchParser.java
M	cadpage-private

[33mcommit ea7cdcd0963b382d948af7e3b6851c05bf058bac[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Dec 16 03:51:05 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 02a3a5630a26db666915446fdd4a459a2a5b93a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 15 18:52:45 2017 -0800

    Release v1.9.15-06

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 5b7286ce57ecb9e4b30b7aa593e19e51b10a73a6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Dec 15 03:49:07 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit b12e0d40cb3c536f6c89689a724fe5e7b1e00a8a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 14 22:13:18 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit b47a256bfa91381cc41a4c809ea32e7e809d358f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 14 21:59:03 2017 -0800

    Fixed parsing problem with Prince Edward County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAmeliaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACampbellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACarrollCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGalaxAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALunenburgCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VANewKentCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPrinceEdwardCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARockbridgeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARockinghamCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASurryCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchDAPROParser.java
M	cadpage-private

[33mcommit d264fcd47154d9f6ec68f6dc2c78cf2ce69c20b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 14 19:15:04 2017 -0800

    Fixed parsing problem with Cass County, ND

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
M	cadpage-private

[33mcommit b5eda060d8bdec75f27de3c6a6d9659fea003588[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 13 17:39:51 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 395628e23fa414eb1023139e0a0ac97caec47c70[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Wed Dec 13 15:56:28 2017 -0800

    workworkwork

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAStephensCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYRowenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMiamiCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXEllisCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPageCountyBParser.java
M	cadpage-private

[33mcommit cff62b5b4695708a8b144b54602f020bbf66a836[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 13 15:42:48 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 4d7a7d56848f848ccdada0becb8674e7bd4daffe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 13 15:12:44 2017 -0800

    Fixed parsing problem with Genesee County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYGeneseeCountyParser.java
M	cadpage-private

[33mcommit 44365da5ec6fecd1f3e6cd670be640f47ba2cdd8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Dec 13 02:39:13 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 1cc123ac4a5a59da49b00af39ab7adeb3d4614d8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 12 22:24:19 2017 -0800

    Added Columbiana County, OH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHColumbianaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHColumbianaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHColumbianaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyAParser.java
M	cadpage-private

[33mcommit 55715cea37bf52c91c72bba6177cca9165f0abec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 12 16:47:05 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit bf28fc86001d62892bc6ba9bec7c8eb9f29d441e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 12 16:36:17 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 19a5a3294eb509d92e1164c948c14a86c77a8fa4[m
Merge: 02ec8b3 9a61609
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 11 22:19:50 2017 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 02ec8b3cb8fbfeb515a4d85b70880744be58b9b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 11 22:19:28 2017 -0800

    Fixed parsing problem with Livingston County, NY (A & C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLivingstonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLivingstonCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA5Parser.java
M	cadpage-private

[33mcommit 9a61609a9431a046a473d610f91a4f6b507e3282[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Dec 11 15:27:17 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 62e05141a778d0721e89c9e96b42f133aa831c83[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Dec 10 03:00:21 2017 -0800

    general updates.

M	cadpage-private

[33mcommit e78c54f28bd5c8f918d92a33a5b78d8c84473634[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Dec 9 15:45:45 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 0570a7e9910ab2c5ab25bcc89d0847e22f151d9b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 9 15:39:08 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 788a0234ec3a495c3fd67864f8d7d37684d98ca9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 8 21:59:24 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 378751c5b5c2d49f10e77e6df566ccc0cf469d61[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 8 21:50:10 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 81c346dc8a152025d5cad3954773232d4de2fc23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 8 21:37:49 2017 -0800

    Fixed parsing problem with Marion County, OR (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyAParser.java
M	cadpage-private

[33mcommit 92cbec5037721717fb83dda3306657e2f643db95[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 8 21:00:16 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 2c065c12aa55118d2425329cfea14049b8208da1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 8 20:38:49 2017 -0800

    Fixed parsing problem with Cascasde County, MT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyBParser.java
M	cadpage-private

[33mcommit 16645a4fbbe15c51896b34b23d310d2bd0676bdf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 8 19:20:09 2017 -0800

    Fixed parsing problem with Hyde County, NC (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHydeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHydeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHydeCountyParser.java
M	cadpage-private

[33mcommit fe8fbc458e58b380a3be84f97abde7ff1739f808[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Dec 8 02:56:36 2017 -0800

    general updates.

M	cadpage-private

[33mcommit b8a95e3978db931cd1fbc85751ebb1a180788be8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 19:37:29 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit fc045b7538f17b51667feca7b241b890d82cfc2f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 16:06:31 2017 -0800

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHarrisonCountyAParser.java
M	cadpage-private

[33mcommit 9a6260b377661158b72fee42e9d456760595a322[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 16:04:44 2017 -0800

    Update call table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit a77a97aefda0caa3a042fbe75ff2a0e6de254f32[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 14:10:44 2017 -0800

    Fixed parsing problem with Tulsa, OK (Add C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaParser.java
M	cadpage-private

[33mcommit c4121dd648e391448b3dc3a5b3006817503ca0de[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 10:59:19 2017 -0800

    ditto

M	cadpage-private

[33mcommit ac528fd34105b950b7b53b20f7268bb607625459[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 10:57:27 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 7b86d5df30cd209ba9d07579e51397b2cd072ed1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 09:09:53 2017 -0800

    Release v1.9.15-05

M	build.gradle
M	cadpage
A	hs_err_pid18623.log

[33mcommit 7f7a59d92320f8397d10bc0dac6d733eb9577412[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 7 08:21:34 2017 -0800

    Fixed parsing problem with Nassau County, NY (I)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyKParser.java
M	cadpage-private

[33mcommit 38f11876285a09720d4b16565d72eaf11b6a1035[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Dec 7 03:44:57 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 66f2ca28674161aa11c3a24255ff736ac0349c35[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 6 21:46:59 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 7e9a4928d8f174b160a5b9dd9a349df459f47ba0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 6 21:32:15 2017 -0800

    Fixed parsing problem with Montgomery County, OH (Added C2)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyC2Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyParser.java
M	cadpage-private

[33mcommit f6de4f91a3bd8f5d6587dcf86a27c3471988063b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 6 18:57:25 2017 -0800

    Fixed parsing problem with Nassau County, NY (K)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyKParser.java
M	cadpage-private

[33mcommit a9f09bd1ccb17d4bde201097e48dbc9ecd8a60ce[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 6 17:57:02 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit de8f8cfaaefda7543bb060c0fcff73d5682e9eb1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 6 12:51:51 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit f56df16bf3f7d98c856a3108296c07958b78a77f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 6 09:45:01 2017 -0800

    Fixed parsing problem with Kent County, MD (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDKentCountyBParser.java
M	cadpage-private

[33mcommit d5b48ef62a166e14a99fd782eec4a61be01d9c05[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Dec 6 03:48:35 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 0efff0be162baa178cc279c0569f3453a9b44529[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 5 19:15:54 2017 -0800

    Fixed parsing problem with Jackson County, MO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJacksonCountyBParser.java
M	cadpage-private

[33mcommit 82b5299a4b72520c813db664e33b1fb59dd3a25d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 5 14:23:27 2017 -0800

    Added support for ArcGIS Navigator

M	cadpage

[33mcommit 781600cf876779012908fcde872cdfed1418301a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 4 10:30:15 2017 -0800

    Release v1.9.15-04

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit d32c9c23377c8a0a9dbe50fc12b34c6813f79850[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 3 21:04:11 2017 -0800

    Fixed parsing problem with Kent County, MD (A and Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDKentCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDKentCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDKentCountyParser.java
M	cadpage-private

[33mcommit 10ee52896e17fe4c82917a10dbb6312e3506ef50[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 3 19:49:09 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 9517b53e0e6dde16277c8ee4a1570df15add78e0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 3 18:54:10 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 6bee131b0b2878525ea4521f47568457c87c77b8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 3 18:07:15 2017 -0800

    Added Litchfield County, CT (C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
M	cadpage-private

[33mcommit 33762d2dcd4882be5a384343011ee396da30c930[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 3 15:02:12 2017 -0800

    Fixed parsing problem with Booone County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBooneCountyParser.java
M	cadpage-private

[33mcommit 70159963fa8e596dd3c4440b1015c3faefdff197[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Dec 2 00:18:21 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 9f48a6a0f67af944d66dede550126201b4e8fe7a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Dec 1 00:25:05 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 3a8b6ee5a5bf38e56c3cfd8b9f63218fd35cdbf2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 30 20:09:48 2017 -0800

    Update sender filter for Bergen County, NJ (F)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyFParser.java
M	cadpage-private

[33mcommit 18e770088141452c4b43b7ebc4f58f6cb497e56a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 30 19:54:11 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f6a902f60ac606a9f34f9fcf1293eae07cfc2cf8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 30 19:46:24 2017 -0800

    Added call code lookup table to Spartanburg County, SC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSpartanburgCountyBParser.java
M	cadpage-private

[33mcommit ea0ac2e43ab8ce29eafa23d68de0c10364aca418[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 30 18:22:31 2017 -0800

    Fixed pasing problem with Bucks County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyBaseParser.java
M	cadpage-private

[33mcommit f35974e9df34edb8d9c42bbc7d87b1645c3e0cd1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 30 11:18:09 2017 -0800

    Release v1.9.15-03

M	build.gradle
M	cadpage

[33mcommit 7db58272efb20ab682206761c8d759b10017b33d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 30 11:13:08 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 64f910de763c372b4ddb6d0da3fe6de584233b1d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 30 10:35:06 2017 -0800

    Fixed crash in scanner timeout logic

M	cadpage

[33mcommit 9e32eacf1f26c5a926f64f6b7da74f5fd93afc95[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Nov 30 00:17:25 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 1874dbf9de7c6c61a20d382f393b529e156bc08e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 29 11:35:24 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 1c5cec716ef242307ef7ac46f19cbc36162c7c92[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 29 11:25:07 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit e995e1d60922f361eb55dfe87b522d52b6c9ca6f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 29 09:02:07 2017 -0800

    Release 1.9.15-02

M	build.gradle
M	cadpage

[33mcommit 662e9273a8ed1f52854deb83d5ddc15097d611d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 29 08:23:17 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 8e08072098500d92f74813e1ce0cdd9435b0edb6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Nov 29 04:25:14 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 6292153c26dc5b3464cddd894b6032e7409e7d77[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 28 17:08:54 2017 -0800

    Update Swedish resources

M	cadpage

[33mcommit 9fc9323e75678ddbdd5e9e899ca2b3745a77841c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 28 15:15:09 2017 -0800

    Fixed parsing problem with Howell County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOHowellCountyParser.java
M	cadpage-private

[33mcommit 136a7e4eee3c872ef1d0f7754815685760e68f18[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Nov 28 14:28:17 2017 -0800

    general updates.

M	cadpage-private

[33mcommit de1288b462528735a8d2df0b7bf0bda4bb0caf7b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 26 12:57:37 2017 -0800

    Release 1.9.15-01

M	build.gradle
M	cadpage

[33mcommit 114822d749a7376936a6456dce757afa8f3c259f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 26 11:45:26 2017 -0800

    Implemented scanner radio timeout

M	cadpage

[33mcommit 35b8219fd06088f8d4e8d46555bc01145ae1ceac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 26 09:13:09 2017 -0800

    Fixed sender filter for Steuben County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSteubenCountyParser.java
M	cadpage-private

[33mcommit 8fce8168acfe078783c240358f02a29cec1734e3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Nov 25 00:27:00 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 9dc6ada04904d50e76ce7cd571e036f847abd974[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 19:18:15 2017 -0800

    Merge Welcome Menu feature

M	cadpage

[33mcommit 984146ffb92c28462d64142290bb236331aee39e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 17:52:57 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 29638cc141dc649ffa57dabc269ac81913dd7cf3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 17:39:51 2017 -0800

    Fixed minor parsing problem with Ascension Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAAscensionParishParser.java
M	cadpage-private

[33mcommit e92342e01f1ae72615a2337b40e4ce112adbbee1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 17:23:47 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 0093f9fc12bbb2ccb52465a2a2a90cb2f988ace0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 17:10:45 2017 -0800

    Fixed parsing problem with Darlington County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCDarlingtonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 0582bada5f6ae240e78ac2a04268c885b8f58a24[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 16:32:32 2017 -0800

    Fixed call table entry for San Bernardino County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyAParser.java
M	cadpage-private

[33mcommit fa7f2adbbfc4d0d12e6424bafd935560b73be79d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 16:11:11 2017 -0800

    Fixed parsing problem with Douglas County, MN (Added B)

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDouglasCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDouglasCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDouglasCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHaysCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA70Parser.java
M	cadpage-private

[33mcommit 8aeffa5772e53974d7969c0b8f1fa6120ae8f8d0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 24 11:23:53 2017 -0800

    Fixed parsing problem with Pitt County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchOSSIParser.java
M	cadpage-private

[33mcommit 2dc72b324fc9767b5ab6bd6bba9869c3acc2297c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 23 03:29:15 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 0b1e3b1c2b5ea2927358d832eaaf4184c2ba3edc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 23 03:18:29 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit f73e6da4f86599a890ee824954f22661d6544e3f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 23 03:07:16 2017 -0800

    Fixed parsing problem with Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit 6ce33dd9e6888366d41c5094039ed39aff099a14[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Nov 22 21:24:32 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 87dd8fa5d120e337bde2efaa6379793cab63f97c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 22 18:26:42 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 8ec7af4ff9496b5d62e9d6d99258c97a22d116eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 22 18:01:59 2017 -0800

    New location, Howell County, MO

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOHowellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 69154300f863652151a1c290108a573ee74f7add[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Nov 22 03:48:15 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit d8f94fe4cbc74e6aa14720e6ec1d0138fff7b9e5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 21 21:03:32 2017 -0800

    Fixed parsing problem with Linn County, OR (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit d8d71211ceb25bddee4ebeede89c36622effaaa3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 21 19:41:45 2017 -0800

    Fixed parsing problem with Marion County, OR (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyAParser.java
M	cadpage-private

[33mcommit 911ced09e54e82de2ed833ddab8998a792f4204f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 21 18:31:54 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 8420054c90c0c20ea186257e8c4f4ea7993c330a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Nov 20 00:52:18 2017 -0800

    general  updates.

M	cadpage-private

[33mcommit 048aed92c4e3b7933d31abdd3caf5e0925b25cfb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 19 14:15:51 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarrollCountyCParser.java
M	cadpage-private

[33mcommit 340a672eba6ed21afed5d014014864ff0ad20049[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 19 14:00:45 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit bb730002e422fe268e5f3623d24d82d382864d86[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 19 04:09:33 2017 -0800

    Fixed parsing problem with San Bernardinoo County, CA (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MONodawayCountyParser.java
M	cadpage-private

[33mcommit 5839cbed0643b02545b26c5716dc82402ad88fdb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 18 09:34:50 2017 -0800

    Release v1.9.14-13

M	build.gradle
M	cadpage

[33mcommit 4f895da331fde923d1b445d875b86587d08f9a5c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 18 08:05:42 2017 -0800

    Fixed parsing problem with Mecosta County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMecostaCountyParser.java
M	cadpage-private

[33mcommit 0e69ad512b7aa42157691108d6ac4b299f720b29[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 18 07:23:59 2017 -0800

    Fixed parsing problem with Mecosta County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMecostaCountyParser.java
M	cadpage-private

[33mcommit f1a36cd7033f3e74eccadbbdca78b8369d8e322e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 17 21:27:58 2017 -0800

    Fixed parsing problem with Madison County, IN (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMadisonCountyCParser.java
M	cadpage-private

[33mcommit 31e94ee5028af9f18b97df65d4fabb89bfcd943a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 17 20:15:38 2017 -0800

    Update sender filter for Allen County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYAllenCountyParser.java
M	cadpage-private

[33mcommit c3deea6c718a336ab978a84e90d18a183fbe8f70[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 17 20:11:40 2017 -0800

    Update GPS lookup table for Adams County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit f67c89697642360e4fb4a15c5c8a97b7c310206e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 17 19:28:41 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit d96bd98e403794db2f13e1707935d4730171d243[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 17 18:38:25 2017 -0800

    Fixed parsing problem with Marion County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyAParser.java
M	cadpage-private

[33mcommit 447793ae21b91d72b254dc14b727bf89a4767328[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 17 17:36:56 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit eeb7de9c54096e8c06260832966fb657fa902a84[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 17 16:58:04 2017 -0800

    Fixed parsing problem with Decatur County, GA (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADecaturCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADecaturCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADecaturCountyParser.java
M	cadpage-private

[33mcommit b012574a4f17e2f5fee8fb4756a7d1291fcb24f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 16 18:12:01 2017 -0800

    Fixed parsing problem with Robeson County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRobesonCountyParser.java
M	cadpage-private

[33mcommit e38f03ae74ee975a51f2594323faec6d821aefe5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 16 14:02:46 2017 -0800

    Fixed problem with Benton County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORBentonCountyParser.java

[33mcommit 73485aaf54293fa89b41e17674875f36fc893301[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 15 17:37:41 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 9d0a165325b5624e026fc9a2ea76dc2ddefbfb5d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 15 17:36:39 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 4ff6275a28a194c87b5a7599b1bc9f5ecae321f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 15 17:18:01 2017 -0800

    Fixed parasing problem with Emanuel County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 116a9cd906d53b269ed9a2686dd847fccbb0b029[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 15 16:39:07 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 35b7d2d7a1395b3906b85a8c524ffb0d2ad9245b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 15 16:20:04 2017 -0800

    Fixed parsing problem with Calaveras County, CA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA69Parser.java
M	cadpage-private

[33mcommit 0f2049658f789560d86b693c8ee4639e463fe6ae[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Nov 15 04:03:49 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 0b836f3ab052dcb4408936e87628dde31e2e01d6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Nov 14 13:17:24 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 9e956c2646a2e72c86a559b24d8daf51b8fa4db4[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Nov 13 15:23:34 2017 -0800

    general updates.

M	docs/support.txt

[33mcommit 2dedc9a785acdeef24e245a31eb72ed32e8d526d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Nov 12 02:09:38 2017 -0800

    general udpates.

M	cadpage-private

[33mcommit f8c8581786ea3e89b5f80764ac6ea31b601e981f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 11 08:53:53 2017 -0800

    Release v1.89.14-12

M	build.gradle
M	cadpage

[33mcommit d8edaae40656bf3d8f62d0c80ecd245820c5ffe5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 11 07:19:26 2017 -0800

    Updated sender filter for Suffolk County, NY (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyBParser.java
M	cadpage-private

[33mcommit 6b47ae5c410e956a529a7f22d0fc5b30ef28e965[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 10 10:49:45 2017 -0800

    Fixed more parser problems

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA52Parser.java
M	cadpage-private

[33mcommit af1a79bc0106215053dac7813f9a59a15073f489[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Nov 10 03:12:38 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 16e15c403e02f62fcb44455292020eace831bd07[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 18:58:24 2017 -0800

    Fixed parsign problem with Spartanburg County, SC (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSpartanburgCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSpartanburgCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSpartanburgCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA52Parser.java
M	cadpage-private

[33mcommit 10e55ad037c8b2329bae23eaf37a39f4efd04cf2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 17:00:15 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyEParser.java
M	cadpage-private

[33mcommit f61989704bdaebc3682217104c623919c999d813[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 16:42:06 2017 -0800

    Update call table fro Emanuel County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 1b49c285d555ebd2825080e0ab62c50b4eab21b7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 16:34:00 2017 -0800

    Fixed parsing problemwith Chatham County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAChathamCountyParser.java
M	cadpage-private

[33mcommit 870494c8ae08c13cb3424f531e2864b733d6e457[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 16:03:08 2017 -0800

    Updated call description table for San Bernardino County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyAParser.java
M	cadpage-private

[33mcommit 7417ca67726a487c3806136a236d31ab2ec3517e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 15:17:16 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java

[33mcommit 644955a95510c735d16bba8abcba94c764243623[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 15:16:00 2017 -0800

    Fixed parsing problem with Delaware County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYDelawareCountyParser.java
M	cadpage-private

[33mcommit fc80b204123592458cdaa86d1002a22e876478b5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 9 12:59:20 2017 -0800

    Fixed parsing problem with Brunswick County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABrunswickCountyParser.java
M	cadpage-private

[33mcommit 5debdd412ea3242739995a146a16efe6cc70a7d7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 8 21:53:29 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 518f1e881da9ec01dca34bc28a9b5fbace6c6dc1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 8 21:29:29 2017 -0800

    Fixed minor parsing problem with Baldwin County, AL (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBaldwinCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 9df4d95964b18b8fa045e1524a92fbf30a0432c4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 8 20:41:11 2017 -0800

    Fixed parsing problem with Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit ba81614d0746528c130f29340c32f8da4fcced25[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 8 18:16:38 2017 -0800

    Update A911 parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 6978ef33979a6863b5b4292a0ace2098e0fc2a61[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Wed Nov 8 14:54:07 2017 -0800

    test

M	cadpage-private

[33mcommit cccaba2f71a186203660913a02db78fc9df06f32[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 7 18:38:23 2017 -0800

    Release v1.9.14-11

M	build.gradle
M	cadpage

[33mcommit 8ee61cad9a45cad8e6c682d494e0d5b553329d0e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 7 17:39:35 2017 -0800

    Updated GPS table for Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 27edece13dbe155f438e7a24d145d032dde97da9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 7 17:02:07 2017 -0800

    Fixed parsing problem with Baldwin County, AL (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBaldwinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBaldwinCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBaldwinCountyParser.java
M	cadpage-private

[33mcommit ada483c5b272c58332e15d4988e3e091136911c6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 7 16:18:10 2017 -0800

    Fixed problem with Wayne County, NC (Added D)
    ,

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWayneCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWayneCountyParser.java
M	cadpage-private

[33mcommit ef3f61baec558a785ced75adfc75f2175b6dce78[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 7 15:32:28 2017 -0800

    Fixed parsing probelm in Marion and Lincoln COunties, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyBParser.java
M	cadpage-private

[33mcommit a6ca9b98bfbc80465bb396264e384c6d2e3555f6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 7 11:06:58 2017 -0800

    Fixed scanner radio problem (agin)

M	cadpage
M	cadpage-private

[33mcommit 7e0f52f7465cd9a30040db3918a7325f4852fac0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 6 20:34:58 2017 -0800

    Checking in new parsers

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyParser.java
M	cadpage-private

[33mcommit 59182452bbf13967e8aefad2307693ac0677c657[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 6 18:14:46 2017 -0800

    Fixed parsing problem with Rockwall County, TX (Added C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRockwallCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRockwallCountyParser.java
M	cadpage-private

[33mcommit 469ac2a468c4c062b4dd47b7d1cf54476650878e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 6 16:12:48 2017 -0800

    Fixed parsing problem with Chatham County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAChathamCountyParser.java
M	cadpage-private

[33mcommit 0e6d486fc708920ec3c6a30f1b31b762dba49308[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 6 13:47:58 2017 -0800

    Update parser docs

M	docs/CadpageParser.txt

[33mcommit 3b4cb3659c8160998a7ff32cab833f3b6067d277[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Nov 6 00:52:34 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 930fe2b13dfe5cce211f74b3769dfb5d18a4b4cf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 5 21:19:16 2017 -0800

    Fixed problem with payment menu screens not scrolling text properly

M	cadpage

[33mcommit 30e2e996edf6c774123b0d5cb91b5a433e59127a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 5 12:46:40 2017 -0800

    Release v1.9.14-10

M	build.gradle
M	cadpage

[33mcommit b63320ecf16d74136488be0d6a35c963ce1a419b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 5 12:15:22 2017 -0800

    Update sender filter for Cuyahoga County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-private

[33mcommit d172c54384ec189bfd1f0da8aab2af953840b742[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 5 12:09:42 2017 -0800

    Fixed more parsing problems with Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit 97f0c308694c7bbe417ad4cd0520589afa6ffd71[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 5 09:02:37 2017 -0800

    Fixed parsing problem with fort Bend, TX (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFortBendCountyAParser.java
M	cadpage-private

[33mcommit 029d63f98e70553e6ebdf285c14becf6d6b539a6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 5 08:52:08 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 25bdcdf6dbcf54180293dab4f05ef6bd48e657e9[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Nov 5 00:10:34 2017 -0700

    general updates.

M	cadpage-private

[33mcommit a3cc2e9a9d6b7c82d861b07d6827212941ff289c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 4 19:16:08 2017 -0700

    Fixed parsing problem with Galveston County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGalvestonCountyParser.java
M	cadpage-private

[33mcommit 9715ef3bddf6b2495fce16ffdc3d9f133052796b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 4 17:44:27 2017 -0700

    Fixed parsing problem with Licking County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLickingCountyParser.java
M	cadpage-private

[33mcommit 4a1b29a18ec07dff40e81790d8c73fd655e8f2ce[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 4 12:43:10 2017 -0700

    Update sender filter for Cayuga County, NY (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyCParser.java
M	cadpage-private

[33mcommit ee9118669f254d1785370139740abf262292b99c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 4 12:33:58 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 4bda644854f38ebd16062f02136ba54f7e761380[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 3 19:54:26 2017 -0700

    Fixed parsing problem with Monroe County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMonroeCountyParser.java
M	cadpage-private

[33mcommit bf0e01aba0eda0aabea2597a0e3bd80207db8cf9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 3 16:45:22 2017 -0700

    Fixed parsing Marion County, OR (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyAParser.java
M	cadpage-private

[33mcommit a6af79f9abf83570132eed8813ca25659b865832[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 3 10:17:41 2017 -0700

    Merged Shaker Heights, OH into Cuyahoga County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHShakerHeightsParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-private

[33mcommit 5eb4ea01287513bdad3628b5c0d502187f4ec1eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 3 09:50:12 2017 -0700

    Fixed parsing probelm with Galveston County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGalvestonCountyParser.java
M	cadpage-private

[33mcommit d243c746c3484477cb68fd9dc71bf90002b9fce3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 3 08:20:16 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 3882b78cf5e5f355df444626f85c5ec4cdd76ee6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Nov 3 01:27:05 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 12120162d91d268de2d084f71265713500334029[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 2 22:02:50 2017 -0700

    Fixed parsing problem with Fort Bend COunty, TX (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFortBendCountyAParser.java
M	cadpage-private

[33mcommit 526e9e46d033404b2294de36a0f3c862937f9bc1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 2 21:23:00 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 490d13fff1b4be6f587e4fb7ae34c49b0e468995[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 2 21:07:30 2017 -0700

    Fixed parsing problem with Buncombe County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBuncombeCountyParser.java
M	cadpage-private

[33mcommit 5271c845a1e03ec3bffbcabe74f4f24f950aac15[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 2 20:12:48 2017 -0700

    Fixed parsing problem with Otter Tail County, MN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNOtterTailCountyParser.java
M	cadpage-private

[33mcommit 2bf675ab1c7de151fc5d7f7b0132423b8a5169a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 2 19:10:11 2017 -0700

    Fixed parsing problem with Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit dd28d8011f78333b354203df7a5a740b8307c4ef[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Nov 2 01:38:47 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 5134df2b3d016c5f64f964df262eb704458875e0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 1 18:21:51 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e94de5191d93e634268c7aef9528bfcb2cff0756[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 1 17:31:21 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 3bde207c5197ef155b4da242897263b9e699a751[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 1 17:13:41 2017 -0700

    Fixed parsing problem with Minnehaha County, SD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDMinnehahaCountyParser.java
M	cadpage-private

[33mcommit 9c54eb72c5c21af7b7e843a2362ddd918121d7d6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 1 14:12:28 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStClairCountyCParser.java
M	cadpage-private

[33mcommit aa6c1eaf6cef4adfca1442cb1171c8c5f2d21382[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Wed Nov 1 14:10:49 2017 -0700

    skeles

M	cadpage-private

[33mcommit 7f24d49f2cf216958f2a56f5c78168cdd7703d1a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 1 08:13:18 2017 -0700

    Release v1.9.14-09

M	build.gradle
M	cadpage

[33mcommit 2643bb026492c1988a05d0dee5ffdf455f4da809[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 1 07:39:25 2017 -0700

    Fixed parsing problem with Roanoke County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCountyParser.java
M	cadpage-private

[33mcommit 82acebe2c54ba29b5d4255004532161034d6d285[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 31 20:55:46 2017 -0700

    Fixed recursion problem in permission logic
    Fixed parsing problem with Cuyahoga County, OH (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyParser.java
M	cadpage-private

[33mcommit 9c62672db931df401020d1220690de2aa1d1b9c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 31 10:45:26 2017 -0700

    Fixed problem launching Scanner Radio in Android 8

M	cadpage
M	cadpage-private
M	scripts/gpull
A	scripts/gpull~

[33mcommit 37095f5d2d12fb4322dc09684cc37413ddc705ff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 30 15:39:39 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit eb787c54b2eca5216f62221c2f1091b0051bcc73[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Oct 30 15:01:14 2017 -0700

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit 790545b0cd8c8cc20dbd9b140bad47a1ca6d9d1d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 29 16:20:22 2017 -0700

    Fixed test class

M	cadpage-private

[33mcommit 548408dba8e23de5c684cecb2fe89306227e4b84[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 29 15:41:48 2017 -0700

    Fixed parsing problem with Cuyahoga County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-private

[33mcommit 319d8a8e09c9d5fc7fbb72f20184a5d14275a7fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 28 18:27:23 2017 -0700

    Release v1.9.14-08

M	build.gradle
M	cadpage

[33mcommit 26a66afe09ecf213cd924a42740a140578a76063[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 28 18:17:25 2017 -0700

    Updated standard ProQA call description tables

M	cadpage-private

[33mcommit 22b4bdf0a15e0dde7d30c06713b8c55c6c73585c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 28 17:34:00 2017 -0700

    update genome.log

M	cadpage-private

[33mcommit 9381f09515e36714e360379fea52c01d0ff2778f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 28 16:48:21 2017 -0700

    Fixed parsing problem with Lackawanna County, PA (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/StandardCodeTable.java
M	cadpage-private

[33mcommit e46a2e1ea4385bfe55d78dc8d30f0eb5f5e78b13[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 28 15:26:37 2017 -0700

    Fixed parsing problem with Houston County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAHoustonCountyParser.java
M	cadpage-private

[33mcommit 10cafff5f48742f53c3203bceba6ee44c160821e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Oct 26 23:35:13 2017 -0700

    general updates.

M	cadpage-private

[33mcommit e58819e7171202c1e684ac63513dfe9df0f0dbf8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 26 21:43:22 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit ba63aa70a254b1be94b29149a56854885db2e05f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 26 08:15:18 2017 -0700

    release v1.9.14-07

M	build.gradle
M	cadpage

[33mcommit 2b00769216a108b6de45a6a0e3f1c979af322178[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 26 08:03:44 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 6f04cf8b40f8d798a95f0ad5487967cb7c9fffb8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Oct 26 01:12:46 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 668a6a9523b2a0295455dea1937454bfde1c0dab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 19:05:01 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyRedcenter2Parser.java
M	cadpage-private

[33mcommit 12e4034d621477fcf1e8f84811ddf75f56d3d8bd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 18:20:53 2017 -0700

    Fixed parsing problems with Thornton, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COThorntonParser.java
M	cadpage-private

[33mcommit 75e42e3cea72cbd652a87e62f1aed430182a9280[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 17:11:20 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 89bd6b4d6c8df7ccc6c4885a62de1b68d563053e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 16:43:59 2017 -0700

    Fixed parsing problem with Emanuel County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 4d473b0b6e17682b6e5562e53f5ec8e282325dc5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 16:13:48 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e8319289460e2bd254828a96d015bef5bdd43cf1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 15:56:02 2017 -0700

    Update A911 Parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit ae422e63bceb101413b0bee5ffec31fe34221dd5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 15:39:44 2017 -0700

    Update A911 Parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYTriggCountyParser.java
M	cadpage-private

[33mcommit 2edfd7b349c45efdc3a06db4e1065ad63334ba23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 15:23:14 2017 -0700

    Fixed parsing issue with Stark County, OH (RedCenter2)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyRedcenter2Parser.java
M	cadpage-private

[33mcommit 6a2fb5a833faefe1d96767a5ca6a27dfa265041e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 12:11:00 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 67b70f566a0c419e2fe1435c6cefbc0ab803176d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 25 11:43:15 2017 -0700

    Fixed parsing problem with Stark County, OH (Redcenter2)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyRedcenter2Parser.java
M	cadpage-private

[33mcommit c90327a22f7398656c110771604f1f2e1cc65ae4[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Oct 25 02:59:25 2017 -0700

    general updates.

M	cadpage-private

[33mcommit cad176c08bb49b61324adee1ea4abeded6323e5a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 24 21:55:51 2017 -0700

    update GCM protocol doc

M	docs/GCMProtocol.txt

[33mcommit c519dd795f4367271a4c45a04cf9952260c1d32f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 24 18:10:28 2017 -0700

    Fixed parsing problem with Cayuga County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyCParser.java
M	cadpage-private

[33mcommit 7b68b3aac6d63903c762c1478c670fa8c7722629[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 24 17:47:31 2017 -0700

    Fixed parsing problem with Caroline County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACarolineCountyAParser.java
M	cadpage-private

[33mcommit 10d62fb76a092e6372d624cb7155ea3dfce6c443[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 23 11:55:38 2017 -0700

    Release v1.9.14-06

M	build.gradle
M	cadpage

[33mcommit b05ef330bfdb2733d84b670a0141b39ee56f42f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 23 11:12:54 2017 -0700

    Fixed parsing problem with Saginaw County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MISaginawCountyParser.java
M	cadpage-private

[33mcommit 8547b340ba4f13a055e1f0b88f80ce8eedd05cb4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 23 10:53:52 2017 -0700

    Fixed parsing problem with Cayuga County, NY (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyCParser.java
M	cadpage-private

[33mcommit 668c4535ca488bc8a0e851b057cdb359bb0a994e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Oct 23 00:59:15 2017 -0700

    general updates.

M	cadpage-private

[33mcommit f205030920149d92889fc6730aa0948e7bb25f64[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 20:23:03 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit a9c8e7c2c1eca56ab6b50ffd6a7f3fa417e7fd7e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 20:21:07 2017 -0700

    Fixed parsing problem with Fannin County, GA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAFanninCountyBParser.java
M	cadpage-private

[33mcommit 8800f906d0fa7458ec95b5951205df903ed0a615[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 20:10:41 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 0f4f2d866995df6df69bc64100cafb01ef11b1dc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 19:52:36 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 489399df683930c991dbcbc8e72efb3f3778ca1d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 19:38:12 2017 -0700

    Update sender filter for St Tammany Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStTammanyParishBParser.java
M	cadpage-private

[33mcommit 95367b7ce0b3942ae6962cfb84cce96705b59dbe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 19:31:11 2017 -0700

    Cleaned up stuff

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-private

[33mcommit 455d7b8a102348701a870241bcf6563016aa2973[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 19:10:40 2017 -0700

    Fixed parsing problem with Cayuga County, NY (Added C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyParser.java
M	cadpage-private

[33mcommit e39ed16ff5b4819efeeba3990638b03afda93f19[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 18:35:19 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 92325cf7b51c9bb7bf17e249a78869adaa534eb3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 18:29:46 2017 -0700

    Fixed parsing  problem with Jefferson County, AL (Added J)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyGParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyJParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GroupBestParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit bf934ab12e40b299d6fb4a26591d1de69fb2d483[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 22 13:21:38 2017 -0700

    Fixed parsing problem with Cayuga County, NY (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyAParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit 05f30b3d145b7e16dce1f85888ef4c5ae8fba4a6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 21 19:20:53 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 2c19b342fa0d4e2e367770a67fa2850d04b6263f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Oct 21 00:59:07 2017 -0700

    general updates.

M	cadpage-private

[33mcommit ab90c70b5c0a63c6d71ff9e6e72ceacc58ca8717[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 20 15:32:05 2017 -0700

    Update sender filter for Harris County ESD1, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyESD1BParser.java
M	cadpage-private

[33mcommit 16cec85be1fc58308e44bd91ff53dad5a875bf1f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 20 14:43:39 2017 -0700

    Update call table fro Emanuel County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 59fa05923dc3a032fc4e0316faa9cfcf71344f03[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 20 13:34:04 2017 -0700

    Fixed parsing probelm witih Montgomery County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMontgomeryCountyBParser.java
M	cadpage-private

[33mcommit fd1d42971b23066e097d24c69724a7898428477f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 20 09:32:54 2017 -0700

    Fixed parsing probelm with Kanawha County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVKanawhaCountyParser.java
M	cadpage-private

[33mcommit 7150747bc2b2679d5def98873fdb3bbd3dd324d6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Oct 20 02:13:36 2017 -0700

    general updates.

M	cadpage-private

[33mcommit addcf2b524a1a13e6a261410b85de0bc0802f05f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 19 09:35:38 2017 -0700

    Fixed parsing problem with Pulaski County, MO (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPulaskiCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPulaskiCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPulaskiCountyParser.java
M	cadpage-private

[33mcommit d764c28daba0396e2f6e3e470ceddd82c3a99017[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 19 08:24:27 2017 -0700

    Added GPS Lookup table to St Louis County, MO (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyEParser.java
M	cadpage-private

[33mcommit 630af402b49ffd1067fcc49f3cd6dc166b6e2258[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 19 08:09:54 2017 -0700

    Update sender filter for Halifax Couty, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHalifaxCountyParser.java
M	cadpage-private

[33mcommit 60b3fdf873a0add5f6ff10ff5cace8c8acf0e50e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 19 07:41:07 2017 -0700

    Update call table for Frederick County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDFrederickCountyParser.java
M	cadpage-private

[33mcommit 71209a553bcee45d7610c645b9191a44cafa5e93[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 18 19:10:50 2017 -0700

    Updates stuff

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMontgomeryCountyBParser.java

[33mcommit 40657f6dc82a5a6e2edd9fa681eea22e9edaff40[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 18 18:44:28 2017 -0700

    Fixed parsing problem with VAMontgomeryCountyB

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMontgomeryCountyBParser.java
M	cadpage-private

[33mcommit f8612f4bb22497edd6034cc22ac3075e831ae654[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 17 22:13:04 2017 -0700

    Fixed parsing problem with Napa County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CANapaCountyParser.java
M	cadpage-private

[33mcommit 37a63bb962d7b5004c9c8815536d422ec9a63bde[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 17 18:36:32 2017 -0700

    Checking in Stark County, OH (Alliance)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyAllianceParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-private

[33mcommit 463b5e281c7c234332a63e61a81ae06747060da7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 17 17:55:32 2017 -0700

    Checking in Butte County, CA (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAButteCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAButteCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPerryCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA33Parser.java
M	cadpage-private

[33mcommit 188dbf1501a869aafbd4cbb0197f62bfb888ad95[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 17 10:21:53 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit b0e130a4e81aafde0f4151378d55c0fd0e19ab64[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 17 10:06:39 2017 -0700

    Checking in Shelby County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILShelbyCountyParser.java
M	cadpage-private

[33mcommit 7578ef4eb1b5256e25a7cb2f1a93438cd935985c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 16 14:57:26 2017 -0700

    Checking in Westmoreland County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWestmorelandCountyParser.java
M	cadpage-private

[33mcommit 85066d51ecf9668606672e64105851de76f06a4d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 16 13:30:25 2017 -0700

    Fixed parse failures

M	cadpage-private

[33mcommit 18bb5be215e03de59051fb281a785b5c3eb3b4f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 18:51:10 2017 -0700

    Fixed parsing problem with Douglas County, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODouglasCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyMetroWestParser.java
M	cadpage-private

[33mcommit fd2e0a0dfd2a6a84d4389d8ad6e7e3434829c2ed[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 18:16:41 2017 -0700

    Fixed parsing problem with Hampshire County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHampshireCountyParser.java
M	cadpage-private

[33mcommit 28e3adb6397b1c3e00dfc969eb7df5665f2c040a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 16:30:12 2017 -0700

    Release v1.9.14-05

M	build.gradle
M	cadpage

[33mcommit 3fcfa4d48c179fcce63ff1c4f5883d14051ac599[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 16:16:30 2017 -0700

    Fixed problem with Cadpage recalculate donation status without email account info

M	cadpage

[33mcommit 64b22ff59f60327a410979a0e36a325b297d2b7b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 15:43:34 2017 -0700

    Fixed parsing problem with Charlotte County, FL (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCharlotteCountyBParser.java
M	cadpage-private

[33mcommit 63d5d9ba519731809529aa75fbdfb6894506b0e1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 15:13:14 2017 -0700

    Fixed parsing problem with Rockingham County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMcDowellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARockinghamCountyParser.java
M	cadpage-private

[33mcommit 29ab957cfbd55609f437ef45b39e8adc8ff8b25c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 13:22:32 2017 -0700

    Fixed parsing problem with McDowell County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMcDowellCountyParser.java
M	cadpage-private

[33mcommit 1cd6c52fe79ff19dff93ee5c2705af68d1662fc2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 10:15:35 2017 -0700

    Fixed parsing problem with EmmetCounty, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIEmmetCountyParser.java
M	cadpage-private

[33mcommit c865e60a532f848fe8fc7ccfb16047fe27d6c354[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 09:47:27 2017 -0700

    Fixed parsing probelm wtih Craven County, NC (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyCParser.java
M	cadpage-private

[33mcommit 78b49161ce92e1a8c0dcb41c821180c5cce4064c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 15 09:01:53 2017 -0700

    Fixed parsing problem with Onslow County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCOnslowCountyParser.java
M	cadpage-private

[33mcommit 57d826cb011c57324f2eb12a9fc89d2cb86101de[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 14 18:17:31 2017 -0700

    Fixed test issues

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDuplinCountyParser.java
M	cadpage-private

[33mcommit 9f38254a04c2e0dbba0cd588bfa8aafa5f5410a1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 14 17:49:54 2017 -0700

    Fixed parsing problem with Somerset County, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Message.java
M	cadpage-private

[33mcommit 5b4028b6dbad01ea3fe2901d8674789d423624db[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 14 17:18:18 2017 -0700

    Fixed test class issues

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPrinceGeorgeCountyParser.java
M	cadpage-private

[33mcommit 9caeeefad90b67a45c5229fe0413cfa8f7f4706f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 14 11:50:51 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCFranklinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyEParser.java
M	cadpage-private

[33mcommit 7baf66f6f2fe9ab36d1d657f4437c5ec49d4dda9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 14 10:37:12 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 55b343d0b1d5bb5fc0e419b65f70201eca9f830b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 14 10:09:57 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 730f18124c46b3b0f4310d958fe9505271ac530b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 14 10:00:54 2017 -0700

    Fixed parsing probelm with Cowlitz County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WACowlitzCountyParser.java
M	cadpage-private

[33mcommit e1aec14b90c84fcac6357d188f51ace7a3aead53[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 13 20:52:59 2017 -0700

    Fixed parsing problem with Marion County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit a4cb69148b98b230410538b817ad57e4b87c1b7f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 13 19:47:53 2017 -0700

    Update A911 parsing table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 08d701a21c0c8eafeec658bafe8039dfdbe72785[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 13 18:29:08 2017 -0700

    Fixed parsing problem with Randolph County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRandolphCountyParser.java
M	cadpage-private

[33mcommit b5d7c384fced1895bcd9e66f6d9063ad9d4aef03[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 13 18:04:15 2017 -0700

    Fixed parsing problem with Pok County,NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPolkCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRandolphCountyParser.java
M	cadpage-private

[33mcommit 7806219e991105398df23df62c01ac97b2ce8460[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 12 21:38:15 2017 -0700

    Fixed parsing problem with Bexar County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBexarCountyParser.java
M	cadpage-private

[33mcommit 5e1a9272444b93869d39c8a539ed32d6b0b51afa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 12 21:15:18 2017 -0700

    Fixed parsing problem with Bergen County, NJ (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyCParser.java
M	cadpage-private

[33mcommit 06505b7f7657fdc43b5ab47e7adebbfe2b8e89fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 12 21:02:11 2017 -0700

    Fixed parsing problem with Cherokee County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCherokeeCountyParser.java
M	cadpage-private

[33mcommit a0dd479b3441b5d61bc389296500aedbf456da7e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 12 18:29:28 2017 -0700

    Misc fixes

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMorganCountyParser.java
M	cadpage-private

[33mcommit 3b556ce1f110adfa6170b40895290cf7bfc93fe5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Oct 12 14:15:45 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 37d8061d060f92dee24a320c44a5465261c2f5eb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Oct 12 03:53:39 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit c448219efecb752c1aa08dabc203a6055b4858f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 20:43:58 2017 -0700

    Fix A911 parser table again

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
M	cadpage-private

[33mcommit 73994bed82bc255a2006ac3bfca86244659033b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 20:39:21 2017 -0700

    Moved Active911 parser table to parser project
    and fixed a number of problem with table entries

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARSebastianCountyParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARSebastionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911ParserTable.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICacombCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMacombCountyParser.java
M	cadpage-private

[33mcommit ecb51e4cc1f0f8a57bea961c64b98fa78b4db1f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 18:27:00 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit aaf994fc0de28a370cff8c6148b8c9b797c6c868[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 18:23:57 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 0db8a86235cd5e9e65ff46ac5ae0a77cf9cd3167[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 18:18:39 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e54f5b5dafb2358e59682627af8c6d23d715bc33[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 18:10:44 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 8999a2060366851678a8356d7e0bccff30ef00d3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 18:03:49 2017 -0700

    Update msg doc

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMcLeodCountyParser.java
M	cadpage-private

[33mcommit 978b1e83181893e47e680785f0e7d06e3cf2fb97[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 14:17:10 2017 -0700

    Release v1.9.13-04

M	build.gradle
M	cadpage

[33mcommit d6ec777ef8534c7352209e799915508fcfd10731[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 13:01:17 2017 -0700

    Fixed parsing issues with Kent County, MD

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDKentCountyParser.java
M	cadpage-private

[33mcommit 5b6b609a150ae0cd4fdd949f4c0a23c232f71560[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 11 09:54:56 2017 -0700

    Fixed parsing probelm with King County, WA (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODouglasCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyParser.java
M	cadpage-private

[33mcommit 3b8ee2b470ee8c955daf3bbdb6cecf644880dbc6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 10 15:50:35 2017 -0700

    Fixed force close in Herkimer County, NY (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA48Parser.java
M	cadpage-private

[33mcommit c050643ef01b16a12c855e93b05f2324725c533e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Oct 10 02:24:09 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 9990f9d20999babca436f2e374f1fc45ce8eebb2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 9 14:49:05 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 71a1bd8aa35322773ee096ea9a54c61d02c6327e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 9 13:38:12 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 7142a59a96496a58ad1ca3b81a0e7b82a991b32d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 9 11:02:26 2017 -0700

    Release v1.9.14-03

M	build.gradle
M	cadpage
M	docs/CadpageParser.txt

[33mcommit 445bae32f53200649bc9721f1faa706a6108837e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Oct 9 02:30:43 2017 -0700

    general updates.

M	cadpage-private

[33mcommit eb58e2acb1ed7a1562ef2c64dc235bbb7484243a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 23:46:04 2017 -0700

    Updated GPS table for Klamath County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORKlamathCountyParser.java
M	cadpage-private

[33mcommit 320b39a7ccb6abf765b867eb10e7c0bad6d07c45[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 23:32:02 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 199e5d28617fb9e40240df5d5b12a65c38f37184[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 23:04:29 2017 -0700

    Fixed parsing problem with Fannin County, GA (Added   B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAFanninCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAFanninCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAFanninCountyParser.java
M	cadpage-private

[33mcommit b9fbe1cb136dd8f96eb0862412aa6b8aa020bf86[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 22:31:51 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit e00ce4c5c11519d0ed519d379279cb0e74c516ff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 20:15:35 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBuchananCountyAParser.java
M	cadpage-private

[33mcommit 4cc3e77418753bb20ee76e31b2abb85e666d61d1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 20:01:24 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 4cf5473c95dcd99e2b95680e40683d8ce5987c59[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 14:53:57 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 741765afe3894ac4c8671cae1ffc958cd9e42edc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 14:50:35 2017 -0700

    Fixed parsing probelm with Boulder COunty, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyBParser.java
M	cadpage-private

[33mcommit d921341c563ea4b0a0e038672a067ae65de3736a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 8 14:12:54 2017 -0700

    Fixed parsing problem with Bucks County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyAParser.java
M	cadpage-private

[33mcommit 76e227b98efcd33704a7262eb3ce8781d0d4361f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 7 06:26:39 2017 -0700

    Fixed parsing problem with St Charles County, MO

M	cadpage-private

[33mcommit 7bbf20c0fe57b63ac8bb6a8e2c6a6627c6de189f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 7 06:22:36 2017 -0700

    Fixed parsing problem with St Charles County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStCharlesCountyParser.java
M	cadpage-private

[33mcommit 13d0a6f1a73d1d7cdf73d39dac2f52b03c988e0a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 21:07:19 2017 -0700

    Fixed parsing problem with Surry County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCSurryCountyParser.java
M	cadpage-private

[33mcommit 7477840cc3f88ec5bc7e024689ec39d9ce7e49e7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 17:05:52 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit a69fb889a484d1d2e2d5f8872cd31f5b59abd10a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 17:00:27 2017 -0700

    Updated GPS lookup table for Adams County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 4b830c014f80f59c5845bba718d17a66251f8347[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 16:18:04 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit dba2f573f7d1d40954e962e8a77acb8085f67477[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 16:07:41 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 85f8bffbe9db5b55b4bf6ae1966bc23b14fa8b3e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 15:57:01 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCamdenCountyParser.java
M	cadpage-private

[33mcommit ee81e3095c41b35375ab0a5678cd6b2d5655efac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 14:02:12 2017 -0700

    Fixed parsing problem with Minnehaha County, SD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDMinnehahaCountyParser.java
M	cadpage-private

[33mcommit 55a76a941135c4aebfcf473a113ead54c2342376[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 6 12:05:43 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1624f80791a68f2a37bbb165d22ad5832f31f356[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Oct 6 02:56:52 2017 -0700

    general updates.

M	cadpage-private

[33mcommit a36c9f77534d29ae41fc6fefd1e9b0f951f46d10[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 5 21:15:19 2017 -0700

    Fixed parsing probelm wtih Iredell County, NC (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCIredellCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCIredellCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCIredellCountyParser.java
M	cadpage-private

[33mcommit cbb242ea48591287375a649ec9ad7a151d903d17[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 5 19:32:21 2017 -0700

    Fixed parsing problem with York County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCYorkCountyParser.java
M	cadpage-private

[33mcommit 2b6c04ab5683063d1599542ac5c355c854180b9b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 5 18:51:06 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 4abac19e05bdc3812530377ab46ef333afc90a47[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 5 15:52:23 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 114c9027151068dfe78b57f248a19700d02fc12e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 5 09:29:26 2017 -0700

    Release v1.9.14-02

M	build.gradle
M	cadpage

[33mcommit 8c6944142cce031b6002d7868c8062d46fbd83fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 5 08:59:18 2017 -0700

    Fixed parsing problem with Hudson, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHudsonParser.java
M	cadpage-private

[33mcommit 1d1f18c1d1e5e1f67a2462e5be3457eeeefd94e1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 5 08:52:26 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit f8a8451e42bd8f2c2705a02d7fc32ce5702957cb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Oct 5 01:04:40 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 6c350f3028eaa764b252d1911eb55373f22e0cf1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 4 22:05:17 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 72c4aa806cd9df39d9cd7393b75717b547a3dcf8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 4 20:35:15 2017 -0700

    Fixed problem with recalcualte payment status not turning on account access

M	cadpage

[33mcommit 94dce2d1fa9405eea676dd478e6681d4d2cf37bf[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Oct 4 02:26:24 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 106f2ea7119eb2df863d3f1821cf3665299b4749[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 3 18:11:20 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 7781795438f52d630710c85752d9ffa45e953056[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 3 11:06:16 2017 -0700

    Fixed parsing problem with Kanawha County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVKanawhaCountyParser.java
M	cadpage-private

[33mcommit e24a4eee73b7c3b0921f3fa3259ca7526a10eeef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 3 09:40:37 2017 -0700

    Release v1.9.14-01

M	build.gradle
M	cadpage

[33mcommit 21b54e1ddbd77f4150f1c9d7e859af2a4ec1fdb4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 3 08:53:12 2017 -0700

    Merge Account Security features

M	cadpage

[33mcommit 3d20283cfbed88a453470ce31cd79562da40e639[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Oct 3 03:55:32 2017 -0700

    general updates.

M	cadpage-private

[33mcommit ca54bbe0dc80d38a47d6133d63beaf0c47995d55[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 2 16:00:47 2017 -0700

    Fixed parsing problem with Columbia County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAColumbiaCountyBParser.java
M	cadpage-private

[33mcommit 754468948993c7ec38889266fdf1d5288235a1cc[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Oct 1 02:13:38 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 9e696f80c2d5ce7b8b8369744a5ec468e90de8d7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 30 13:20:57 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 508db29d6141136e842f728ec70518883f7b95fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 30 11:19:36 2017 -0700

    Update unit pattern fro Montcalm County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMontcalmCountyParser.java
M	cadpage-private

[33mcommit a847c65e322da0f15a4f38db973d7dc0bd1e47fe[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Sep 30 02:23:33 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 7275fce37b217b09d6eaec5c1d2273e2122dfea4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 29 20:10:00 2017 -0700

    Fixed parsing problem in Suffolk County, NY (G & add L)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAllParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyLParser.java
M	cadpage-private

[33mcommit cdbe9ac20cc88c9b30d5024e8d8bd9c81782a9ec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 29 18:09:00 2017 -0700

    Update city table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCColumbusCountyParser.java
M	cadpage-private

[33mcommit 866b4096996e69dc5e4e5603cbdc0ef2d1d49bd9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 29 16:25:22 2017 -0700

    Fixed parsing probelm with St Joseph County, IN (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyBParser.java
M	cadpage-private

[33mcommit 16e2c4e8a5a30bd876e66910866422da4a5e48d1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 29 15:31:55 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 9bc4e3ff693d5b98750af1589ad46f65be912a85[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 29 15:03:36 2017 -0700

    Fixed parssing problem with SUrry County, VA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASurryCountyParser.java
M	cadpage-private

[33mcommit ab36288acee8085ef76edc8cf10ad93feed569d8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 28 09:55:47 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCOnslowCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGreenwoodCountyParser.java
M	cadpage-private

[33mcommit dc461636c05d2462e3ce043cc88b4e2e527f64ba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 27 22:07:49 2017 -0700

    Update Mobile County, AL again

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMobileCountyParser.java
M	cadpage-private

[33mcommit b4cb65482fcc97ea80a2cf956036b130e26c2396[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 27 22:04:07 2017 -0700

    Fixed parsing problem with Chilton County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALChiltonCountyParser.java
M	cadpage-private

[33mcommit c619cafd5237d1c9537d5dfecb6e3c718e3f5459[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 27 20:45:20 2017 -0700

    Fixed parsing problem with Mobile County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMobileCountyParser.java
M	cadpage-private

[33mcommit ef7a56d3bd772cfa108b08e57caf5acf35a74f3f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 27 19:05:08 2017 -0700

    Fixed force close

M	cadpage

[33mcommit 559293e3fecbdf941870a3e1841316b5702c8586[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Sep 27 03:40:58 2017 -0700

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit cf69527751a9c3f311f310f87bf09c62beb8162c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 25 19:46:12 2017 -0700

    Update sender filter for Nassau County, NY (K)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyKParser.java
M	cadpage-private

[33mcommit b3325f67e1a7de68117ba308af04c08561856c65[m
Merge: b71ecf9 26f1331
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 25 17:52:36 2017 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit b71ecf9b96735fb0dc54ef993f40b14686d58db5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 25 17:52:10 2017 -0700

    update to latest version of gradle

M	build.gradle

[33mcommit 26f13311c484374f9215c284fce5513391324818[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 25 12:12:47 2017 -0700

    Releae 1.9.13-26

M	build.gradle
M	cadpage

[33mcommit c58263e42b56f36cb022564164f8fa4da6c8b087[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 25 11:12:35 2017 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNEdenPrairieParser.java

[33mcommit 278c16c445dee8f828013dcf9f4aa622921f8c9d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 25 10:57:41 2017 -0700

    Fixed parsing problem with Eden Prairie, MN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNEdenPrairieParser.java
M	cadpage-private

[33mcommit bd4f3a308abcf2efdc48bdc3013ddc124f3a815e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 24 14:50:13 2017 -0700

    Fixed parsing problem with St Charles County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStCharlesCountyParser.java

[33mcommit 6dc6529f66bbc6b8a8d0f80ff33979b506731aa3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 23 20:25:03 2017 -0700

    Fixed parsing problem with Butler County, OH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHButlerCountyBParser.java
M	cadpage-private

[33mcommit 1b614cd43be7f30ecf9b0f140dd5fb0ea82dd9ae[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 23 12:08:05 2017 -0700

    Fixed parsing problems with Jefferson County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJeffersonCountyParser.java
M	cadpage-private

[33mcommit ea51da94638e9bbe07c9f9cf6798c34875bcc796[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 23 11:30:14 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 05a905fcf861df0db6350aade92f31019ef3586e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 23 11:23:47 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishBParser.java
M	cadpage-private

[33mcommit 08160cc7422724cf9cb3e64b88c9352fbc7caf6b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 23 11:18:59 2017 -0700

    Fixed parsing problem with Northanmpton County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSRileyCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthamptonCountyParser.java
M	cadpage-private

[33mcommit a757d974f6a714ed7be3654c500fbb30ff49f5e8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 23 10:24:30 2017 -0700

    Fixed parsing  problem with Johnson County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSJohnsonCountyParser.java
M	cadpage-private

[33mcommit 1cbd1e0d4283088039f934cb0437abb9b380f647[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Fri Sep 22 17:03:22 2017 -0700

    skeles and parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAButteCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILShelbyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INJacksonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPerryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLakeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyCParser.java
M	cadpage-private

[33mcommit 60ba92d4ebd6cca945eb757de6c585ab889a7e59[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 22 12:02:05 2017 -0700

    Release 1.9.13-25

M	build.gradle
M	cadpage

[33mcommit 3ccce775318d94e3959135ba17f80753330e4f5d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 22 11:19:29 2017 -0700

    Fixed parsing problem with Citrus County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCitrusCountyParser.java
M	cadpage-private

[33mcommit fd6bf0ebbf82ada43db713bf597bd259edbb4a8f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 21 16:18:03 2017 -0700

    Fixed parsing problem with Sullivan County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSullivanCountyParser.java
M	cadpage-private

[33mcommit bce608868cd6958624a5132485431a4de5682775[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Sep 21 00:22:41 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 029da4fa7a774de0be0f92b8aeab836d325baa35[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Sep 19 16:38:04 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 8422ae7be2d5562f91703014622bcae82432052b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Sep 18 22:12:46 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 7e4140497c938316673511c51a578e262593ecef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 18 21:23:14 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 43d8862421b8e3037fe43f15cc4f2f8440d7ccab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 18 16:34:07 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 99bf5bc46604bdbcccbcf93a84f5099af6eb81b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 18 16:16:03 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 18e8e61d21fd6bef333f2e2fd92fc8458e216746[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 18 16:07:27 2017 -0700

    Fixed parsing problem with Boulder County, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyBParser.java
M	cadpage-private

[33mcommit dd482eae85384c56a4fe88e8095351a5752d8264[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Sep 17 23:55:33 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit a754b5b83d4d5fef8e9f113e426ebf7a57960c0a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 17 15:01:43 2017 -0700

    Update sender filter for Leon County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLeonCountyBParser.java
M	cadpage-private

[33mcommit ee8f45312be4024711b5850762d98aa54b350723[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 17 14:45:36 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 414aa5a277abc018b9385ac3095da733d7f3494d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 17 11:09:44 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyBParser.java
M	cadpage-private

[33mcommit 748d5e58406d4bbd7c15a566ad571e7899f907bb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 17 10:30:50 2017 -0700

    Fixed parsing problem with Montgomery County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMontgomeryCountyBParser.java
M	cadpage-private

[33mcommit 620bc63bbfc6ddd27fe6ea8c92606d202db0162c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Sep 17 01:43:38 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 1e41e33af649137996aee7fb4892e0aa76db9088[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 15 18:16:37 2017 -0700

    Checking in DuPageCounty GPS lookup table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyParser.java

[33mcommit 4def0c25966722c6c46d5140426468af13e8f3d3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 14 15:59:29 2017 -0700

    Update sender filter for Bradford County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABradfordCountyParser.java
M	cadpage-private

[33mcommit 70f0b49ace4d2fa4ec7012285c8d6a754a949612[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 14 15:57:30 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1324298c67cbd8eee1e610a01f5df2bee17dcf20[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 14 15:41:14 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ea18b3b27cdb425a55afb80739cb6d33ffd25dd2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 14 15:19:02 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKlickitatCountyParser.java
M	cadpage-private

[33mcommit 2854f8c84a31b7729df61a2c558eaca6b8310511[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 14 08:14:23 2017 -0700

    Fixed parsing problem with Macoupin County, IL

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMacoupinCountyParser.java
M	cadpage-private

[33mcommit ff90c9c7e53f09a8ef2b04ee1ecc934a5070e13c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 13 19:23:03 2017 -0700

    Fixed parsing problem with Washtenaw County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIWashtenawCountyParser.java
M	cadpage-private

[33mcommit d14c8ada3a77b9dea8a5a5ddc3e3e35f8846f937[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 13 17:44:09 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1257adae83929d313d7a4c9ff8b2df0b905c10c6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 13 10:59:14 2017 -0700

    Release 1.9.13-24

M	build.gradle
M	cadpage

[33mcommit a6b9649ecd1a6de91f459c9f68cc9eaf3176bad8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Sep 13 10:29:19 2017 -0700

    Fixed parsing problem with Cambria COunty, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACambriaCountyParser.java
M	cadpage-private

[33mcommit c350f54519fb2bb836adc5835d366c9d858e9800[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Sep 13 03:35:24 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 8736b3e24a400cba3cf918f13157ea5cc0e85d80[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Sep 12 21:01:47 2017 -0700

    Fixed parsing problem with Sussex County, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJEssexCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyAParser.java
M	cadpage-private

[33mcommit 14075d743eda7fd4e4c477e4f8d06cc60e448e90[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Sep 12 17:50:46 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit ba01aea4275106602da6ccdbcbd0fb11111aeda6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Sep 12 16:47:53 2017 -0700

    Fixed parsing problem with Mo9nterey County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
M	cadpage-private

[33mcommit c6fa2c87efae99b633fcb9aeb21c5b24f197c58b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Sep 12 12:04:47 2017 -0700

    general updates,

M	cadpage-private

[33mcommit 68c8f4e374e75db2ea2f85d135229326efeb4325[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Sep 12 01:50:48 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit cf2846043f10cc51360eee33a6e69bc1134aff85[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 11 17:40:30 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 6926fc02536f23cc8b0b84ad40caa2dfb28dff0f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 11 17:32:59 2017 -0700

    Update sender filter for Leon County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLeonCountyBParser.java
M	cadpage-private

[33mcommit 84292b2597df4f778751f937ff112c55ee824c6b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Sep 11 00:55:26 2017 -0700

    general updates.

M	cadpage-private

[33mcommit e18ba7c8bb6c385f3b3f0edcaddefa6800e14009[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 10 21:49:57 2017 -0700

    Added Toast messages to show progress of direct paging reconnect

M	cadpage

[33mcommit bf594a9ed20639d1cdd94bc1220d5c2bb067bc3f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 10 20:49:15 2017 -0700

    Active911 communication reliablity enhancements

M	cadpage

[33mcommit 816c6e31a1d0f10a6c2cd3599ea096fc5b9fe4a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 10 08:38:54 2017 -0700

    Release v1.9.13-23

M	build.gradle
M	cadpage

[33mcommit c03493549c7633e65a16a637a62f4993bdb3a05d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 10 08:06:57 2017 -0700

    merge stuff

M	cadpage

[33mcommit 826e7096438fefee5692b8d9dbc747ae66c50978[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 10 08:04:06 2017 -0700

    Checkign in NDBillingsCounty and NDKidderCounty

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDBillingsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDKidderCountyParser.java
M	cadpage-private

[33mcommit 5247f1ed935e56bb862125dfe69b543ef1e9eae0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 21:08:52 2017 -0700

    Update A911 Parser table

M	cadpage
M	cadpage-private

[33mcommit e1b7e0569a50bb3aaf69723aad5691efac19a03a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 16:42:26 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 7a5469fb6e70921dbcaacf9dca6fcfd4253a1495[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 16:34:47 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e078f31684ad661f3cd4a27783950321f5e38458[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 16:27:27 2017 -0700

    Update A911 parse table

M	cadpage
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMorrowCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMorrowCountyParser.java
M	cadpage-private

[33mcommit 56f61b8a5159133a10e1f1546ff563118501bb9e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 15:47:56 2017 -0700

    Fixed parsing with Jefferson County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJeffersonCountyParser.java
M	cadpage-private

[33mcommit 9132d22a73462fc5fb4b88df13eee9e826ef8f26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 13:32:33 2017 -0700

    Fixed parsing problem with Navajo County, AZ (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZNavajoCountyBParser.java
M	cadpage-private

[33mcommit 548c687501ba11b82388d281bef9cfa7daa0e036[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 08:55:17 2017 -0700

    Fixed minor parsing problem with Cabell County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVCabellCountyParser.java
M	cadpage-private

[33mcommit d186e417f2d0424dfc70a276490ccd85d72e4867[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 9 08:47:02 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 8f3cf380e9e8f32f074b935c826228754965851a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Sep 9 02:10:52 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 3eb519b40bed5ca6fa7549fd8c8faa0533c43b9a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 8 20:40:17 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCullmanCountyParser.java
M	cadpage-private

[33mcommit c075104c18040b96400946d63abe0c51e91c6995[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 8 20:18:56 2017 -0700

    Fixed parsing problem with Kenosha County, WI (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
M	cadpage-private

[33mcommit c7b6affd8f2b746cbe651043c6435c5cc67d78e5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 8 16:01:58 2017 -0700

    Fixed parsing problem with Sarasota County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLSarasotaCountyParser.java
M	cadpage-private

[33mcommit b4741c979e7e331e512da87d6bd852d28c342fef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 8 15:11:35 2017 -0700

    Fixed  release date

M	cadpage

[33mcommit d9a2cd5d649e37474cfcad86269021e2efbadf02[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 8 10:10:09 2017 -0700

    Release v1.9.13-22

M	build.gradle
M	cadpage

[33mcommit 136ee4b55b104900f1702def85e09ac838c209a2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 8 09:37:55 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 54e8e66ba3e86028520663de3f455f556d397861[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 8 08:45:38 2017 -0700

    Fixed parsing problem with DeKalb County, IN

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INDeKalbCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INDeKalbCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INDeKalbCountyParser.java
M	cadpage-private

[33mcommit 98a4d20fd83c81b456bf59b69dbdb92d03748850[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 7 23:04:24 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit a9454ac96499c2c612853e359e0ebd6105bc6ca4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 7 22:44:52 2017 -0700

    Fixed parsing problem with Monterey County, CA (A)

M	cadpage-private

[33mcommit 0124c5998438a90b8e2695c15cceb14089bd81b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 7 22:25:39 2017 -0700

    Fixed parsing problem with Monterey County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
M	cadpage-private

[33mcommit e8827168f733af85c9a9c4167675ce266ed8064c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 7 21:26:42 2017 -0700

    Fixed parsin gproblem with Pima County, AZ

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZPimaCountyParser.java
M	cadpage-private

[33mcommit 8e6997a073e1fbdd106d74530841e1611c973402[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 7 21:12:24 2017 -0700

    Fixed parsing problem with JCMCEMS, NJ

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJJCMCEMSJerseyCityParser.java
M	cadpage-private

[33mcommit 17ac5a80e2b094eb62e636166f079a67cccbc276[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Sep 7 13:29:20 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 9d97f69cb14227dfb768626719e1b7e8bee8d78a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Sep 7 01:09:32 2017 -0700

    general updates.

M	cadpage-private

[33mcommit e3ab1459b7346897af4f1a5b98b51d38ba576797[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Wed Sep 6 13:42:11 2017 -0700

    skeles and parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCullmanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNStearnsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKCanadianCountyParser.java
M	cadpage-private

[33mcommit d602ca54f6c3fedb57a83664f3b37abb5bcf66f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Sep 5 17:15:09 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 448a87a0f04e6e2b971ae2874f0650053b89fe65[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Sep 5 16:47:03 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 2e2b64f1a2da06114e3b96d5c3ee73d3f98a636f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Sep 5 08:38:29 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1d72430d69c698166b9340351152aba1d26e0697[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Sep 5 02:28:17 2017 -0700

    general updates

M	cadpage-private

[33mcommit b7f1332b9829faac383588ba7d9f76f6680aa787[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 4 18:49:48 2017 -0700

    Update support.txt

M	docs/support.txt

[33mcommit 56562083e1d6f30a8b296be660579503b778be02[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Sep 4 14:22:55 2017 -0700

    general updates.

M	cadpage-private

[33mcommit e21a1dda28a45a0ba4369d519d231339a9cfd7a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Sep 4 11:05:00 2017 -0700

    Release v1.9.13-21

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit f3842fa495c56107dd157de61baf6473e01fc443[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 3 16:53:53 2017 -0700

    Fixed parsing probelm with Kane County, IL (C)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyParser.java
M	cadpage-private

[33mcommit 4f5545f7541adf950150ac5cc0d946f7a899b587[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 3 08:27:57 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAPikeCountyBParser.java
M	cadpage-private

[33mcommit 13693072ec4d7c3b12e238b7515f1f9beb6b35e0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 3 08:09:54 2017 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyLParser.java
M	cadpage-private

[33mcommit 882cd3b6376aa6449969c2920491735c6a91bc93[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Sep 3 07:43:02 2017 -0700

    Fixed parsing problem with Chester County, PA (L)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyLParser.java
M	cadpage-private

[33mcommit c4890d56df256c53d955c641c917f8eb230f813a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 2 20:05:47 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 934cf37cd843f6733a8033bb47d955c96609d8f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 2 17:52:09 2017 -0700

    Fixed misc stuff

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchPrintrakParser.java

[33mcommit 190e5acb51e533e417fffd766c776de1b022a8b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 2 17:08:34 2017 -0700

    Fixed parsing problem with Orange County, NY (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOrangeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchPrintrakParser.java
M	cadpage-private

[33mcommit 698b8e7bdc53186fb5fb7f71568167348e09e0d9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Sep 2 09:18:10 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit caac58ab34f0de28bd284509000bca14d7630371[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Sep 1 20:06:09 2017 -0700

    Fixed pasing problem with Butler County, OH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHButlerCountyBParser.java
M	cadpage-private

[33mcommit 6f713cc6df91ddbb97ca3045f034ae86d22bd3d7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 31 22:34:19 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 558c45553a5500476a8a8cad156bd27454f5c241[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 31 22:21:03 2017 -0700

    Fixed parsing problem with Augusata County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAugustaCountyParser.java
M	cadpage-private

[33mcommit a45ea834815508c74ae149cc5cdc2aa11b5b079b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 31 21:29:12 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 380fb530e6aae81bceeffdab9c13b28173cea8a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 31 21:19:34 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 2c5cc38fcb8e8a0d20d483fa9637d467ef3ae057[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 31 08:42:33 2017 -0700

    Release 1.9.13-20

M	build.gradle
M	cadpage

[33mcommit 467b987fcfc0799628c564f313500187acb4d31e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Aug 31 03:00:13 2017 -0700

    general udpates.

M	cadpage-private

[33mcommit 78c1ec23d7d47177d1cf233ec827c1bb0bbc32ad[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 30 20:20:08 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit b151bafb6efa1408e51ce7bb78bfcaf7c1e680bf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 30 18:11:26 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit b9c0056620e1aa8a47005d3d4ccd19c9ff2a32b7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 30 18:08:37 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHButlerCountyBParser.java
M	cadpage-private

[33mcommit 4ae66e22fb21863734f7a48a942a1de1faa027bb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 30 14:22:32 2017 -0700

    Fixed parsing problem with Augusta County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAugustaCountyParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit b57f15dc823c3ac5a31af588ee380a4cd2130988[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 30 10:01:34 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit b3e0fa8a9d91f5f94cca1f590445e9a71d857dcc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 29 22:07:49 2017 -0700

    Fixed phone parsing in all DispatchA17Parser subclasses

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA17Parser.java
M	cadpage-private

[33mcommit 8674c03e3adeb19355f0fe5a6fb1e967385c7a00[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 29 19:29:08 2017 -0700

    Fixed parsing problem with Nassau County, NY (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyBParser.java
M	cadpage-private

[33mcommit ea910418d9ae433b5a0e7985ebc73f5e4dd30182[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Aug 29 02:11:32 2017 -0700

    general updates.

M	cadpage-private

[33mcommit db2f37418b98600fd9ca431c021e364727a8eddf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 28 19:16:57 2017 -0700

    Update genome.log

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-private

[33mcommit 5ad0aad050f23c28f5b53b2371a042cc7e9cf972[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 28 18:06:22 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 302a1b1e4fb13bdc51ecf447639c9e1d60fc0418[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 28 18:04:23 2017 -0700

    Fixed possible crash in startup parsing logic

M	cadpage

[33mcommit 206631d509a0160c6420550bd500b2f36d26e49e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 27 17:09:15 2017 -0700

    Release v1.9.13-19

M	build.gradle
M	cadpage

[33mcommit 4d2f4581682297902a66bad1be05c3dbeb9722eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 27 15:14:27 2017 -0700

    Fixed startup app crash

M	cadpage

[33mcommit 2163c87d0834c59959caca2b0b2df257ecf948fd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 27 14:53:03 2017 -0700

    Fixed more problem with Kaufman County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKaufmanCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKaufmanCountyBParser.java
M	cadpage-private

[33mcommit 15b874b2742d36cb4fbbf74655fdbf76ce342893[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 27 10:34:49 2017 -0700

    Fixed crash after launching Active911 in Airplane mode

M	cadpage

[33mcommit 388c0bd24316a59386e94c02a873a998909a7bba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 26 20:32:08 2017 -0700

    Added GPS lookup table to all Allegheny County, PA parsers

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyParser.java
M	cadpage-private

[33mcommit cd838bc8ef02368692d2ee61b9379bb3f2dfd648[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 26 18:32:18 2017 -0700

    Updated call table for Emanuel County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 0fce3cc4a8ef49350fae85918d05d4cf67e6f3be[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 26 18:01:11 2017 -0700

    Updated GPS lookup table for Saint Marys County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSaintMarysCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKaufmanCountyAParser.java
M	cadpage-private

[33mcommit 9a01214e3fa956a48b03ada3d3ebf9de466ad3cc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 26 16:49:30 2017 -0700

    Update call table for Jefferson County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCForsythCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAJeffersonCountyParser.java
M	cadpage-private

[33mcommit 53990314b966530f16d902a5f4a0b600fb602b7a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 26 11:48:09 2017 -0700

    Fixed parsing problem with Kitsap County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKitsapCountyParser.java
M	cadpage-private

[33mcommit 25736d0b70e3c78ad7bb4066fe822e35e0f43c46[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 26 10:49:43 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit fca9eaf904dd8cf9977dce762052278b372b6710[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Aug 26 01:45:36 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 48c3512834318f1aa9c83fa06187e994f3f9e28c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 20:58:21 2017 -0700

    Release v1.9.13-18

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 6e02d3a9db3bf3df4382776816f42cc4e23958c6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 20:06:55 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAMeriwetherCountyParser.java
M	cadpage-private

[33mcommit 394d3c3383488bbe9428d691e059eb9e4eea2de3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 19:12:21 2017 -0700

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHKnoxCountyParser.java
M	cadpage-private

[33mcommit 2275dfdd348ef0312f3fb0c98b4ab49bf719f568[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 19:06:01 2017 -0700

    Update send filte for Montezuma County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMontezumaCountyParser.java
M	cadpage-private

[33mcommit a2dd7208cfcf9adafeaee8837c70e9a9a6b231a7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 18:55:59 2017 -0700

    Fixed parsing problem with Clermont County, OH (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA52Parser.java
M	cadpage-private

[33mcommit ad328cb3308fff361f1dd148e9bc77142819125f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 17:19:24 2017 -0700

    Fixed parsing problem with Linn County, OR

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit 0dd8dc19f0de0017720ebbbae6e1b70f2db2a2fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 11:44:54 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 25b0dee671b4d0e1ff960cf1a4976381c2790771[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 25 01:47:21 2017 -0700

    Fixed parsing problem with Monterey County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
M	cadpage-private

[33mcommit 67c9fc5437f8917df3269f7ccaff690ed849fc87[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 24 09:44:50 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit 5b441f8eedc8641e7e92453ebb3ad1bab1dde649[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 24 09:39:46 2017 -0700

    Fixed parsing problem with Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit 0db28adf72e9f5338e08feb27bb4754dad269d7d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 24 08:54:59 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 6a19aa172bcfb0c76a0dd3207f57f83912ed5f5e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 23 19:49:45 2017 -0700

    Fixed mapping problem with Benton County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORBentonCountyParser.java
M	cadpage-private

[33mcommit f60fff029ef5afb343ab5ed9992c524bef1824c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 23 19:22:22 2017 -0700

    Update city list for Highland County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHighlandCountyParser.java
M	cadpage-private

[33mcommit 74dea789ccfe96ade24e6445fce5667275d2f1c8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 23 14:27:50 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit a86cd10b7e94e9929e7c01b371f754347c177d11[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 23 14:00:45 2017 -0700

    Fixed parsing problem with Bexar County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBexarCountyParser.java
M	cadpage-private

[33mcommit 5708507510b50aef731520be2b791eb5ea1c8f2f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 23 10:22:11 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit bc74605bdf1049816b37dd508db14d20b1af4040[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 23 09:49:39 2017 -0700

    Updated GPS lookkup table for Mid Island Region, BC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCABC/ZCABCMidIslandRegionParser.java
M	cadpage-private

[33mcommit fe20be3615ec3473639bd66390ee6cdfe557e66a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Aug 22 23:45:25 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 5a26101428eb40f5b3e36a9701261f686413b5d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 21:37:14 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 38a8f834d1c063c7ae6c94a8dc903b5c696c9fcc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 21:18:25 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1e9ceb6a4eab854fb349c08cc35201b50fe6678f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 20:37:00 2017 -0700

    Fixed another parsing problem with Sweden

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgInfo.java
M	cadpage-private

[33mcommit 84e236c9771e071d7a22c62202811612022d36b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 19:13:09 2017 -0700

    Fixed problem with Sweden

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZSE/ZSESwedenParser.java
M	cadpage-private

[33mcommit df37a2cc3edefa92755d6da56e28887d564c63be[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Aug 22 11:24:39 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 1bbca9cf2b673a2ecaa33e7fee33b1f1f39b72b4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 10:35:54 2017 -0700

    Release v1.9.13-17

M	build.gradle
M	cadpage

[33mcommit 86a763b45297452902d39d16612c53a33329a13c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 10:02:27 2017 -0700

    Update call code table for Place County, CA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyBParser.java
M	cadpage-private

[33mcommit 97fed0a13a38b91029ed9ea3e6d2d73110760e7d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 09:54:54 2017 -0700

    Update sender filter for Calcasieu Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LACalcasieuParishParser.java
M	cadpage-private

[33mcommit d2a675d1cd18004f01d076b0e9667d12404e3ebc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 09:42:05 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit a47f8c58024de9b6752284479a2916fbaf615639[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 09:29:00 2017 -0700

    Fixed parsing problem with Sweden

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZSE/ZSESwedenParser.java
M	cadpage-private

[33mcommit 026aca6e46dd08feb0244e02c0653f15391279a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 22 08:35:32 2017 -0700

    Checking in Morgan County, MO

M	cadpage
M	cadpage-private

[33mcommit 0cb4a997be0edcf621c62d2e7ca98873f8480a4d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 21 20:13:23 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 16b8b6d2ccd0bd58952c58f8d6e91a788d823305[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 21 19:27:05 2017 -0700

    Fixed parsing problem with St Clair County, IL (A & Add C)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStClairCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStClairCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStClairCountyParser.java
M	cadpage-private

[33mcommit 664aa8f163cf86db3d5a2e08555b69a5adbf4724[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 20 21:52:15 2017 -0700

    Release v1.9.13-16

M	build.gradle
M	cadpage

[33mcommit 4a4075e6bc9f1b2594c39fcda0b73aae1d57eb60[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 20 13:04:23 2017 -0700

    Fixed parsing problem with Geauga County, OH (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGeaugaCountyBParser.java
M	cadpage-private

[33mcommit 04f6b183bcf6a43090864fed1c4a8a90b486c2b2[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Aug 19 23:53:54 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 20428dfc917d9031381c581d19e2cc7837da1a0b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 19 13:37:34 2017 -0700

    Fixed parsing problem with Berkeley County, SC

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCBerkeleyCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-private

[33mcommit 3a5aeca48a32dbcd1988808ce9a58e6af5e603f4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 18 18:49:02 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 9932062008a82c25663ab1a6df6566f767f8b55d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Aug 18 14:30:21 2017 -0700

    general updates.

M	cadpage-private

[33mcommit bbdf4b7a88dfd973df418512377d0a1fbc4c777b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 18 14:22:52 2017 -0700

    Fixed problem with startup performance

M	cadpage

[33mcommit 86df81075221edc07c4bdbb67c0d59325f103da5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 18 14:00:29 2017 -0700

    Fixed possible problem with Active911 reregister logic

M	cadpage

[33mcommit 6f135930b3ad3342a201156bc22f9a4dc19183e8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 18 11:46:35 2017 -0700

    Fixed parsing problem with Suffolk County, NY (H)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyHParser.java
M	cadpage-private

[33mcommit 3df5df74a2a0df6b0b864cbcc2dcfcbc5de88b59[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Aug 18 01:22:01 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 95753b828c6361bd4e566081f0aa718ee8ca7827[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 17 20:50:01 2017 -0700

    Fixed parsing problem with Cherokee County, NC (A&B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeCountyBParser.java
M	cadpage-private

[33mcommit 1cf47512afd46caab1895db7b721c1064237e37e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 16 12:41:05 2017 -0700

    Fixed parsing problem with Kitsap County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKitsapCountyParser.java
M	cadpage-private

[33mcommit b4f652e281d5c1f029ffbbc20c25db956ede7d74[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 16 08:01:59 2017 -0700

    Ditto

M	cadpage

[33mcommit a2670185749631dececcacdfc1964321fc5b3180[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 16 07:58:38 2017 -0700

    Fixed problems with Isle of Wight County, VA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAIsleOfWightCountyParser.java
M	cadpage-private

[33mcommit ac98bbd6e7bfdbb36f297796dd1debc823065ddb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 15 10:23:59 2017 -0700

    Release v1.9.13-15

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA46Parser.java
M	cadpage-private

[33mcommit b8e727d20e8e64201c4694292db88e9c26a031da[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 15 09:08:27 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 73d5aee7385199749300a981c1f6ec7e5ceb3bdf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 15 08:55:02 2017 -0700

    Update sender filter for St James Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStJamesParishParser.java

[33mcommit c98ddf657c98964c40d790655dbcd94986929a1b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Aug 15 02:49:36 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 90fe06f98f433b46c241f376688eaea550b291e5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 14 18:04:21 2017 -0700

    Update sender filter for Overton, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXOvertonParser.java
M	cadpage-private

[33mcommit 29ec4a8e18c3d29eb24f22f19af3819a6fb6fb66[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 14 17:58:52 2017 -0700

    Fixed problems with Boone County, IN (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INBooneCountyBParser.java
M	cadpage-private

[33mcommit cd6a5bdc506aa7ec2063da88050b6458ed73fb96[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 14 15:16:30 2017 -0700

    Updated GPS lookup table for Saint Marys County, MD

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSaintMarysCountyParser.java
M	cadpage-private

[33mcommit f9a776b85c3f24cada5e1acb36d3ca1ddc19cbdb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 14 14:32:28 2017 -0700

    Update sender filter for Cuyaoga County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyBParser.java
M	cadpage-private

[33mcommit 41d184eb232c77929c730204a84d3ba480b654f0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 14 12:17:32 2017 -0700

    Fixed parsing problem with Emanuel County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit dfc328a7c1573dfb6c06c792b580c2c15aa9d4bc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 14 11:02:39 2017 -0700

    Fixed parsing problem with Boulder COunty, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCherokeeCountyParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/SplitMsgOptionsCustom.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyBParser.java
M	cadpage-private

[33mcommit 0168e6f35ca6d0949a5558c8791aaa0ce40294e2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 14 08:11:53 2017 -0700

    Fixed parsing problem with Natrona County, WY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WY/WYNatronaCountyParser.java
M	cadpage-private

[33mcommit 2f37dcb1323700f50be38a6d822d3b303b0dcc5c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Aug 14 01:58:41 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 9e3587cf22a343cf92990a0da45082971ac54626[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 13 19:48:17 2017 -0700

    More work on resolving conflict with Active911 app

M	cadpage

[33mcommit 3e4d052872a5411cbb70346b0c893d5c572b155a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 10 20:51:51 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit f87410ef262503437349878717b8d711141ec5f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 10 20:28:27 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 5229eac11a9648d6402c2f82bc5d03901dd966e9[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Aug 10 03:44:08 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit ada545f407c178f59508b99ccf1add3ae8afb5c8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 8 09:37:19 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 27460a53e7ed4685b829b07fbaba228980e3b652[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Aug 8 03:13:01 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 5c8069aa149dfbbe63c53733b0f4adf45b1e236c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 7 19:05:58 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 5d9ff63e7bca3a65b375de667dff8cb78a651094[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 7 18:40:34 2017 -0700

    Fixed parsing problem with Aransas County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAransasCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA46Parser.java
M	cadpage-private

[33mcommit 32439f4d703f415b02ac87e9b9cc47a5bc8c6b9a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 7 08:48:03 2017 -0700

    Checking in Somerset County, PA (B)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyParser.java
M	cadpage-private

[33mcommit 0116350eef355ab37bf1dae4d25e4cf46556c44d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 7 08:37:45 2017 -0700

    Update Morgan County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMorganCountyParser.java
M	cadpage-private

[33mcommit 3b0775bb33f03e2550943460f292a2bcd079ad4d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 7 07:01:53 2017 -0700

    Checking in Emanuel County, GA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
M	cadpage-private

[33mcommit 1b8ff8c7c539902c1f5979c535c98b703cf01360[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Aug 7 06:52:57 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 25441f85d4c7fa9257d2e91c0b52e9cff2a0f66b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 6 20:08:15 2017 -0700

    New Locations: St Louis County, MO (G & H)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyGParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyHParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyParser.java
M	cadpage-private

[33mcommit 3ebebef07a9b066e8d9f44ab225992cc4eabfec9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 6 18:39:27 2017 -0700

    New location: Los Angeles County, CA (C)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALosAngelesCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALosAngelesCountyParser.java
M	cadpage-private

[33mcommit bac38818e2b8185c543231b7b790976660d674d0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 6 18:27:59 2017 -0700

    New parser Shoshone County, ID (B)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDShoshoneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDShoshoneCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDShoshoneCountyParser.java
M	cadpage-private

[33mcommit 57f4ad06cb9fc1273f9fe2dad6d80d2a846687cd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 6 11:57:39 2017 -0700

    release v1.9.13-14

M	build.gradle
M	cadpage

[33mcommit 33ddd6438e9dc87c8f8695ba50a720479a53d1dc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 6 10:41:22 2017 -0700

    Fixed parsing problem with Union County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCUnionCountyParser.java
M	cadpage-private

[33mcommit a672e919dc527f8d6eed3bccf2fed92234861be5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Aug 6 10:11:17 2017 -0700

    Fixed parsing problem with St Joseph County, IN (Added B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyParser.java
M	cadpage-private

[33mcommit 7aed3d6cf69875eb8ede50f36fdb210db6d7a81a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 5 16:57:46 2017 -0700

    Fixed parsing problem with WIlliamson County, TN (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyCParser.java
M	cadpage-private

[33mcommit e15eb76d258fe65c0da400ba3432fdd75ebe11ff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 5 15:16:43 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAJeffersonCountyParser.java
M	cadpage-private

[33mcommit f7ecbfd35f7972da24258021eacc70fc988451cb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 5 15:09:18 2017 -0700

    Fixed  parsing problem with Rowan County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRowanCountyParser.java
M	cadpage-private

[33mcommit a2593deb08731a651228bb932b95ebe10fe6223d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 5 13:58:00 2017 -0700

    Fixed parsing problem with McCormick County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMcCormickCountyParser.java
M	cadpage-private

[33mcommit 24f0451eb5ef474a1c772e7ff1a87b96f870bd0f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 5 13:11:14 2017 -0700

    Update GPS lookup table for Murray County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMurrayCountyParser.java
M	cadpage-private

[33mcommit 8ff1c376d62eaad6f5c729bbee7cb57da322b06b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 5 10:33:21 2017 -0700

    Fixed parsing problem with Sand Diego County, CA (A)

M	build.gradle
M	cadpage
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavidsonCountyAParser.java
M	cadpage-private

[33mcommit c82c47d64f59874acc4f2141bb373e72d07f437b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Aug 5 09:00:23 2017 -0700

    Fixed parsing problem with San Diego County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyAParser.java
M	cadpage-private

[33mcommit d3ff191cb3abb38f21fec4b3edb8037f1af13539[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 4 17:17:14 2017 -0700

    Update A911 account exception table

M	cadpage

[33mcommit 811ba4a660e90ba34664c3a8d8443d9d13a14a87[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 4 09:28:55 2017 -0700

    Release 1.9.13-12

M	build.gradle
M	cadpage

[33mcommit 2caf3fc311a6b6aea87dc126104eed208f21fa6e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Aug 4 07:56:29 2017 -0700

    Fixed parsing problem with Edgecombe County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCEdgecombeCountyParser.java
M	cadpage-private

[33mcommit 6f89e4f3978f5223b5c634af14d0136a929ea4ab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 3 21:44:31 2017 -0700

    Fixed parsing problem with Gooding County, ID

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA22Parser.java
M	cadpage-private

[33mcommit d1ef80e1d9d44bbead3297f8aec6c55bb374fe40[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 3 20:36:19 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit c4921344567488e99411be465455afbd9fb867ba[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Aug 3 16:47:08 2017 -0700

    skeletons

M	cadpage-private

[33mcommit 2a5abee11accb1e128889837d2d4dfe665ffdc5c[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Aug 3 14:22:47 2017 -0700

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALosAngelesCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEmanuelCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIVanBurenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMorganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyBParser.java
M	cadpage-private

[33mcommit def0a5de13a5ce1be31e5dafd1e361636a69e320[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 3 10:07:38 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCharlesCountyAParser.java
M	cadpage-private

[33mcommit 4a30be7630a4c9a8f4640113331aa6507348bff0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Aug 3 08:48:42 2017 -0700

    Checking in Pima County, AZ

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZPimaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 21230b7579880d4b001b672232d0e06c3954a613[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Aug 3 01:38:53 2017 -0700

    general updates.

M	cadpage-private

[33mcommit c9c4ab37c593a066731565adcfcacc90e67e8055[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Aug 2 19:38:22 2017 -0700

    Fixed parsing probelm with Chilton County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALChiltonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernPlusParser.java
M	cadpage-private

[33mcommit 378f5c4050538a0bbc4d240752507a2097dea46c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 1 09:15:01 2017 -0700

    Release v1.9.13-11

M	build.gradle
M	cadpage

[33mcommit be0f5eca0fe37178890cb8cf45e67c43ffff51cb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Aug 1 08:46:47 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALChiltonCountyParser.java
M	cadpage-private

[33mcommit ce58e7a055283f250b1197fb70694e6769aff2f9[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Aug 1 01:56:59 2017 -0700

    general udpates.

M	cadpage-private

[33mcommit ba269623bfaac8686af0848e6b2e687bb0f72c0f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 20:38:21 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDPenningtonCountyParser.java
M	cadpage-private

[33mcommit 6e9ce450f46340be61a3fd28cdbc20d8bf89a561[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 20:25:51 2017 -0700

    Fixed mapping problem with Sacramento County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASacramentoCountyParser.java
M	cadpage-private

[33mcommit fe651c34e2a14d4a75338f0c31f86de6115badad[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 19:19:27 2017 -0700

    Fixed parsing problem with Citrus County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCitrusCountyParser.java
M	cadpage-private

[33mcommit 0658cccd0fed1f85cd13cc0a6ffd80a49b5ce866[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 14:35:44 2017 -0700

    update genome.log

M	cadpage-private

[33mcommit b5d9a982fa18862001116c66797c18b0a1841fa7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 14:29:59 2017 -0700

    Update sender filter for Pulaski County, AR (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARPulaskiCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCOnslowCountyParser.java
M	cadpage-private

[33mcommit d85bdc44df79ebdd384ad4c227e52c722cb19f67[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 13:31:48 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1d65fb09368a54fc02a512355c5df3628fc1a159[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 13:03:51 2017 -0700

    Fixed parsing problem with East Feliciana Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAEastFelicianaParishParser.java
M	cadpage-private

[33mcommit 90188104e1b5c4621ccfe9524eaf2e4e6e15a924[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 12:57:07 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit f359da0bcdf6d0ae1fa147db7f882e1854a1235b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 31 06:44:05 2017 -0700

    Release v1.9.13-10

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit df1aacb9ec4f02dfb205a53fe9d2cce06c393bd0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 30 20:39:33 2017 -0700

    Update sender filter for Woodbury County, IA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWoodburyCountyBParser.java
M	cadpage-private

[33mcommit 51de49bdb32ef17e667df69bf0f61331ef999123[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 30 20:01:32 2017 -0700

    Fixed parsing probelm with Monterey County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
M	cadpage-private

[33mcommit 7dfdd8f60e6696244697edb29e52bfa907df8bcf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 30 18:57:53 2017 -0700

    Fixed parsing problem with Monterey County, CA (A & B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyBParser.java
M	cadpage-private

[33mcommit 0590ee7051a44ddede44f416850b00f99436156c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 30 17:33:38 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit d8ade9193cb656da3d1c676ec799a4f7d39f1821[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 30 11:58:50 2017 -0700

    Fixed parsing problem with Suffolk County, NY (G)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyGParser.java
M	cadpage-private

[33mcommit 55138a46b32dcd1715dc823c865b84b4e54eec97[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 30 11:06:56 2017 -0700

    Update sender filter for Suffolk County, NY (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAParser.java
M	cadpage-private

[33mcommit 5566023c8c01774db3e8da177f051839acf3d27b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 30 08:41:56 2017 -0700

    Fixed parsing problem with Mesa County, CO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMesaCountyBParser.java
M	cadpage-private

[33mcommit 690f77e1489f7043be39fae75c66951acf5e9a2c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 29 20:53:27 2017 -0700

    Fixed parsing issue with DeSoto County, MS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyBParser.java
M	cadpage-private

[33mcommit b683574fa556d4ae1a702bc6786334f59753b32b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 29 20:40:17 2017 -0700

    Fixed parsing problem with Lake County, CA (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALakeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALakeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALakeCountyParser.java
M	cadpage-private

[33mcommit 58184b0fc97e101acd901b304ad0268c4d5499b9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 29 18:31:11 2017 -0700

    ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCountyParser.java

[33mcommit d6eb2036ee30d4c7a4e8b6da41b794346a2088b7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 29 18:25:55 2017 -0700

    Fixed parsing  problem with Roanoke County, VA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCountyParser.java
M	cadpage-private

[33mcommit 42b5958aac9566d5afb190f95fba8e678521ce3a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 29 15:09:44 2017 -0700

    Release 1.9.13-09

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit a86ae8d4e0e6345278c53b2c6a3de35b819cfb85[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 29 13:40:57 2017 -0700

    Fixed parsing problem with Frederick County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDFrederickCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSumnerCountyBParser.java
M	cadpage-private

[33mcommit c4d689e51c2d3b0a2080f12abefe5e0858db7f5a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jul 28 01:52:46 2017 -0700

    general updates.

M	cadpage-private

[33mcommit cd49500357504bbf381a39447219ad46fbf7588c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 27 14:17:15 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 2db0d5e7bfb3e1841c78bcb8414d4a387a29edea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 27 14:06:41 2017 -0700

    Fixed parsing problem with Sumner County, TN (Added B)

M	build.gradle
M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSumnerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSumnerCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSumnerCountyParser.java
M	cadpage-private

[33mcommit 398b64d13b84d9283268f9e5d45122230a33fea7[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jul 27 03:17:56 2017 -0700

    general udpates.

M	cadpage-private

[33mcommit 5f98e9799c319e3a8601179478a45c25371399a3[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Wed Jul 26 17:21:19 2017 -0700

    skeletons

M	cadpage-private

[33mcommit fcf192fbb76b1de18cd9f584423bfa8eab1f1b6c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 26 08:39:19 2017 -0700

    Fixed parsing problem with Cherokee County, NC (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeCountyParser.java
M	cadpage-private

[33mcommit 92d39e3035878cc7f04f27cd9c48920a45d098c7[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Jul 26 02:57:49 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 415a5660d443c2b20e370c514b4bee372c8715ef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 24 21:31:08 2017 -0700

    Fixed parsing problem with Columbus County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyGParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCColumbusCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 0c57df662a2770e66ba6deaebb3e0ed03e9d2f57[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 24 18:22:56 2017 -0700

    Fixed parsing problem with Guilford Couty, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGuilfordCountyParser.java
M	cadpage-private

[33mcommit 6741c55ce491864addf519cf396ee2d61d9f6bdd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 24 17:56:50 2017 -0700

    Fixed parsing probelm with York County,PA (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyDParser.java
M	cadpage-private

[33mcommit ccb7eeeb00037e7fc474bab97a7bb8da72f19f56[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 24 17:24:59 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 5bd717bb927122688b12e3be4944d207b9a6cc24[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jul 24 00:08:44 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 7831fbc0b597e56e9777f1b4c37091dafc618a1c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Jul 23 02:59:34 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 74db6eca40c86bd657d3b0efb12aa8c397ad51cb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 22 18:16:30 2017 -0700

    Fixed parsing problem with Fulton County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYFultonCountyParser.java
M	cadpage-private

[33mcommit d38869130952689625652ddb2269a9111658dd01[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 22 17:47:54 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit bfb831c910c3039074d93196ddae58ed3c1b3f3a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 22 10:30:19 2017 -0700

    Fixed parsing problem with Francois County, MO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStFrancoisCountyBParser.java
M	cadpage-private

[33mcommit 1824ae4f1ef5925b7afb211bb657066639141137[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 22 09:39:24 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit f54af710cc71f55ae0206ef61d0d6006e98d5b6e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 22 09:34:47 2017 -0700

    Update sender filter for Garland County, AR (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARGarlandCountyCParser.java
M	cadpage-private

[33mcommit 881a8a65d9653cd04e18d2f0741d616a0c67987c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 22 09:24:09 2017 -0700

    Fixed parsing problem with Adams County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 391e2c0e2bdea7de68303947dda960c8d31f9c92[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jul 22 02:39:03 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 207e1a57e28169cc2f012c84cae9f2c1e4466383[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 21:56:28 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit c904210dcf0453ce386bbe481cc89f16cc18dc88[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 21:20:22 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 576a43174fd2c978e836610c6ca62837f9e26bd4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 21:08:07 2017 -0700

    Update sender filter for Vigo County, IN (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INVigoCountyBParser.java
M	cadpage-private

[33mcommit 5b2f4d3c6efb595e708798fd69751436fb935a66[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 20:45:10 2017 -0700

    Fixed parsing problem witih Hancock County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA29Parser.java
M	cadpage-private

[33mcommit 48dd5dbff6722ad48610c97213fecfa4d43b1d95[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 20:09:39 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit f08495d037e7d4df1adbe6397498ef2ad06233c2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 16:47:33 2017 -0700

    Fixed parsing problem with Jefferson Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAJeffersonParishParser.java
M	cadpage-private

[33mcommit 7e5c48e370b8894a89bef8b1c620631f613c6871[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 12:10:11 2017 -0700

    Fixed more parsing problem with Alexander County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAlexanderCountyParser.java
M	cadpage-private

[33mcommit dec771388421ee003ff157ca77361e00b80085ea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 11:17:41 2017 -0700

    Fixed parsing problem with Alexander County,NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAlexanderCountyParser.java
M	cadpage-private

[33mcommit cf6b831b84e61fe83d05dd3fc0f7c9063d946317[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 10:05:10 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 3bb45c81121881401356e2b248683af59dafdb84[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 08:44:23 2017 -0700

    Release v1.9.13-07

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyAParser.java
M	cadpage-private

[33mcommit 536c4175c3709d3998944a8460b84ddbad4c3344[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 21 07:42:40 2017 -0700

    Fixed parsing problem with Elk County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAElkCountyParser.java
M	cadpage-private

[33mcommit ab7f3abd35b71265e4241888354a59dfefd6c186[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jul 21 00:08:04 2017 -0700

    general updates.

M	docs/support.txt

[33mcommit c685fe7573c781c1d94f8a5ae3a5c726766e96ab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 20 20:26:34 2017 -0700

    Fixed parsing problem with Escambia County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLEscambiaCountyParser.java
M	cadpage-private

[33mcommit a29baedb9ed10d9dc64f55549f38b7143ad0e90d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 20 07:34:19 2017 -0700

    Fixed parsing problem with Onondaga County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyAParser.java
M	cadpage-private

[33mcommit c7f196d2a0566e41de0082943c14feb51afa958f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jul 20 01:26:34 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit f38590677fb06b288a3f573d35c862db8eee6d0e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 19 19:54:52 2017 -0700

    Fixed parsing problem with DuPage County, IL (Added D)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyParser.java
M	cadpage-private

[33mcommit 33dd80fa4a83215ba14e6f717618f5a3ae4d2733[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 19 12:30:59 2017 -0700

    Checking in Somerset County, PA (B)

M	cadpage-private

[33mcommit ec07164ec22181f8b001b281fc49c1782668b341[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 19 12:24:20 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 945a641f1f8f73844c8a325d5e47b34655f91da0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 19 12:12:20 2017 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDTalbotCountyBParser.java
M	cadpage-private

[33mcommit f02edc9c1ceb473e383ad9a518c7fa0fe593d77f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 19 11:51:34 2017 -0700

    Fixed parsing problem with Talbot County, MD (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDTalbotCountyBParser.java
M	cadpage-private

[33mcommit b5c93a102d7211175f45b3e660e82defe0e7821d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 19 10:19:15 2017 -0700

    More startup performance improvements

M	cadpage

[33mcommit 1950f50318ca495a6c5a52d8216b7e6381ea4dfb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jul 18 17:41:54 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 2195dd96e315a480019261a99b59d90e11c2ef0b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jul 18 09:09:37 2017 -0700

    Update A911 parse table

M	cadpage
M	cadpage-private

[33mcommit ca4a80a94c0fe377128a74a9747d3a9bbcf99405[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jul 18 02:26:57 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 5752e8202a13ccaffcac1287ab451f88d859a064[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 17 20:35:34 2017 -0700

    Release v1.9.13-06

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 2c8d40c1b8b7477c21024add561e80ea3e439564[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jul 17 00:34:17 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 19f4a25c0635c53e8b636bd1ee8261749e776c82[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 15:27:37 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCarterCountyParser.java
M	cadpage-private

[33mcommit 0b2c320fb580272bac6ed4d47644ea05d3abee4c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 15:06:48 2017 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSPearlRiverCountyParser.java
M	cadpage-private

[33mcommit ef5dcd485412b0126cb71f985f06102eba1627b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 15:04:20 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 33cff884454faa0712a94086a2a9c363338e63f6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 14:45:20 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit db42358d17944c6a756afedc58f25292296b730b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 14:32:27 2017 -0700

    Fixed pasing problem with Jackson County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORJacksonCountyParser.java
M	cadpage-private

[33mcommit 078ae1439c61231db92dcb6e6effd6b574523e3d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 10:04:39 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit a0031846084c8a7c4b2534a69ac1eb8eaf5dce10[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 09:33:40 2017 -0700

    Fixe dparsing problem with Dade County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADadeCountyAParser.java
M	cadpage-private

[33mcommit 69f4c7fcfedb6f717f1e4207fe19cb30f3a7a1f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 16 09:06:35 2017 -0700

    Fixed parsing problem with Colbert County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALColbertCountyBParser.java
M	cadpage-private

[33mcommit 48d59113af399bff5da5c3401d1413517d4e08af[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 14 21:19:07 2017 -0700

    Release v1.9.13-05

M	build.gradle
M	cadpage

[33mcommit 2f082dd888fac58642279df04f014198a844bf00[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 14 20:37:38 2017 -0700

    Update support docs

M	docs/support.txt

[33mcommit 2c2e9afa77802a94302c688d656574a5b79d75ba[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jul 14 01:52:51 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 2a138b91e58148b7427fa9e4addd0abb2c4028d0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 13 08:16:31 2017 -0700

    release v1.9.13-04

M	build.gradle
M	cadpage

[33mcommit ea05da81f03590d6180dbb15154250920b8795c1[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jul 13 02:46:37 2017 -0700

    general updates.

M	cadpage-private

[33mcommit a7048a90984cd5822110a0609a0af4044013f663[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 12 21:42:16 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 5c108572b3dc95820b3b9c87ca3f301c5c4c445c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 12 21:28:18 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit 2cf024def4b6d6a1263b005b6bfe188364b17b35[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 12 20:47:02 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 6d92893d917c0de9ff37df024241d5569ea6683c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 12 20:12:57 2017 -0700

    Update sender filter

A	=
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWhiteCountyParser.java
M	cadpage-private

[33mcommit 338ef0df676d88579cc4b125741d22ba0de2dcd8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 12 19:49:10 2017 -0700

    Removed Active911 conflict resolution trigger

M	cadpage
M	cadpage-private

[33mcommit cfd38b0ddd9a72eab1740769b3b34459d9afecaf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 12 08:56:37 2017 -0700

    Fixed parsing problem with Calveras County, CA (B) & Tuolumne County, CA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA69Parser.java
M	cadpage-private

[33mcommit 7f21dccb2a78fa3375c0d8cd1667de784d312e14[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 12 07:03:27 2017 -0700

    Fixed multiple CT parsers

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTHartfordCountyFarmingtonParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTollandCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTWindhamCountyBParser.java
M	cadpage-private

[33mcommit a141e1fa94a805c86e32204437111b7e31ff18fb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jul 11 03:07:26 2017 -0700

    general updates

M	cadpage-private

[33mcommit f4846b02b0c5d1a861d04738aa289986ebf309bd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jul 10 00:42:42 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 7b45fd5f0cd6a5c4853d8e9332dafabf1f90bda8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Jul 9 00:32:08 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 9a57ef617600b195624d2ce8f9d23d1921f071e5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 21:35:57 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 08066d25e8605b7d72b43ab12f32092b55794601[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 21:17:25 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 2d42b0cd579a745c3357811bf2c0a2f14c25bc5f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 21:12:21 2017 -0700

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishAParser.java
M	cadpage-private

[33mcommit 2f5bb38877c77b7acc8794ee1a8005952a118b76[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 21:09:44 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ccee9b9ff72dcf6866eb3f3bc4a3454605e63c45[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 21:04:54 2017 -0700

    Checking in Henderson County, KY

M	cadpage
M	cadpage-private

[33mcommit 02492ec0a45749d5227c3bdd6fd3a1cfd19cc59c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 20:44:41 2017 -0700

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAKernCountyParser.java
M	cadpage-private

[33mcommit bcf3ad8242d1522286f6fdd6e6e151b6ed50bbcc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 18:44:20 2017 -0700

    Fixed minor issue with ORLinnCountyB

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit be754193f1fbff04c0e4913ae9d70929348057d2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 8 16:58:29 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INNobleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNMorganCountyBParser.java
M	cadpage-private

[33mcommit 5663bc656e549e01bc60f273cb01fa0eb8d5bb15[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Sat Jul 8 15:26:09 2017 -0700

    skeletons and addresses

M	cadpage-private

[33mcommit f02555d76f21b610a82097b892ee68fcf1bb0a67[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jul 8 01:11:53 2017 -0700

    general updates.

M	cadpage-private

[33mcommit abecb6a6dd58ba739fa60ae612e78f988ffbc60a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 7 19:28:04 2017 -0700

    Update unit filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit fbfacf70d1cabad8aeb484436b4ace9ae130c6eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jul 7 18:45:22 2017 -0700

    Moved historical message parsing to worker thread

M	cadpage

[33mcommit 48160364d4d80d987ab0ebe92819730430ccaa0e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jul 7 02:30:14 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit de34c715fa831e738b99dddb2afa63e62595a8c3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 6 09:12:18 2017 -0700

    v1.9.13-03

M	build.gradle
M	cadpage

[33mcommit f5e5ac9f07b336e79322a737ff2836db0f02e80e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 6 08:14:59 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABradfordCountyParser.java
M	cadpage-private

[33mcommit 543ad06211a1ef148b8996882f00577b9d19a2e0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 6 07:48:28 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 25907e158411da7fd772b3042ca02a0268cd5cea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 6 07:39:59 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 3fab601d7fbb9b534f7693b7ab0518287670573c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jul 6 07:27:28 2017 -0700

    Update Harrison County, MS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSHarrisonCountyParser.java
M	cadpage-private

[33mcommit 17bd8264f65fd151d4ab396556a51ee3447d7e61[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jul 6 03:13:53 2017 -0700

    general updates.

M	cadpage-private

[33mcommit a1a614a2226726bfce18dba60251ac0fafff1a27[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 5 21:22:03 2017 -0700

    Added Harrison County, MS

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSHarrisonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 332ca1aeaa8cdc6079f2385b9a83176235295ca1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 5 17:54:14 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit b012a41f0b1bff4d48d438d9b5a7fdf4d1b58ab9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 5 16:13:57 2017 -0700

    Release v1.9.13-02

M	build.gradle
M	cadpage

[33mcommit 167f4ffbc3bbeb7132484a8e0760f0ccf68ccc9d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 5 15:10:16 2017 -0700

    Fixed parsing problem witih Flathead County, MT (A & Add B)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyParser.java
M	cadpage-private

[33mcommit ad6eda2452403869219540f2a3b48fa7355f57bc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jul 5 08:47:58 2017 -0700

    new location: Bennington County, VT

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTBenningtonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTLamoilleCountyParser.java
M	cadpage-private

[33mcommit ca058476e50c2a6f9e7b2d5ab09edd28e0c33929[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jul 4 21:43:01 2017 -0700

    Added Swedish translations

M	cadpage

[33mcommit 1fb43fe520bb7c618f92dd4d1b7f7df973b5e822[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jul 4 09:17:52 2017 -0700

    Release v1.9.13-01

M	build.gradle
M	cadpage

[33mcommit ac402c59d067bed8897eea1fc73407dcad1e0f4d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jul 4 00:35:23 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 047e8c1e13778bc406374cd49f7ce0f9133900c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jul 3 07:41:44 2017 -0700

    Release v1.9.12-15

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 0946baaf385e148bd784f835d7ec9d27404fbf54[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jul 3 01:33:26 2017 -0700

    general updates.

M	cadpage-private

[33mcommit f440b9a581e01a97fed8aa0c488afd5d292f9267[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 22:12:31 2017 -0700

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCoffeeCountyParser.java
M	cadpage-private

[33mcommit cbc6f49c22b4829829e96a3a046116c3048638ca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 22:08:45 2017 -0700

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPulaskiCountyParser.java
M	cadpage-private

[33mcommit 16b583fe65558a1f12f641c04f6875fb4d678de6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 21:55:25 2017 -0700

    Fixed parsing problem with Pasquotank County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPasquotankCountyParser.java
M	cadpage-private

[33mcommit 8b68b5a9bf7fc0eb8d14aaa127217cbd983e1638[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 21:34:56 2017 -0700

    Fixed parsing problem with Licking County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLickingCountyParser.java
M	cadpage-private

[33mcommit c91ab006c89d4db5c1252fe0aad678842dbce83e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 15:20:23 2017 -0700

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKYukonParser.java
M	cadpage-private

[33mcommit 39bc15266d4b005cd1f5406b804c3dfef0536dc3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 15:16:36 2017 -0700

    Update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMarshallCountyCParser.java
M	cadpage-private

[33mcommit bfd75475c65850a4526e680976e54b982e345aba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 15:12:22 2017 -0700

    Fixed parsing problem with Cherokee County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCherokeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/SplitMsgOptionsCustom.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
M	cadpage-private

[33mcommit 918de0c8c342d15c94ac9b9a0140bfd2b34d6674[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 14:10:28 2017 -0700

    Fixed parsing problem with Ste Genevieve County, MO (added B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStFrancoisCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStFrancoisCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSteGenevieveCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSteGenevieveCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSteGenevieveCountyParser.java
M	cadpage-private

[33mcommit be1fd550cbec3bacc9886b57aafeba5125bf7a0e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 13:15:31 2017 -0700

    Fixed parsing problem with Fancois County, MO (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStFrancoisCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStFrancoisCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStFrancoisCountyParser.java
M	cadpage-private

[33mcommit 43e29b19abd42499845123a70f04246a3d95ecc6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jul 2 01:14:38 2017 -0700

    update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCampbellCountyParser.java
M	cadpage-private

[33mcommit 3cf604ae4133a63cefb699577da106ec2912359b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 1 22:28:22 2017 -0700

    Fixed parsin gproblem with Clay County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCClayCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
M	cadpage-private

[33mcommit 6f836c4acbb843621d5ba72ca41da32703b4b45c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 1 22:02:10 2017 -0700

    Fixed parsing problem with Allegan County, MI

M	cadpage-private

[33mcommit a5e9dc65a8127737746aa3242a3ecc33667c235c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 1 21:26:16 2017 -0700

    Fixed parsing problem with Boone County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVBooneCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 44a993a46a0b85f66910a88b5768646aec4c8b38[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 1 12:47:24 2017 -0700

    Release 1.9.12-14

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 4aa73a856541bcba4251e4f8f4398a767da35278[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jul 1 12:11:23 2017 -0700

    Fixed parsing problem with Hocking County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHockingCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 4d8259b253cbdd848c13337f6ea8c511d47ff361[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jul 1 01:36:22 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 873dc920ce082e100b8ffdd084d75739b448982f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 30 21:03:49 2017 -0700

    ditto

M	cadpage-private

[33mcommit c19a2d4deec4f77594a1226ed4f0cca3ba074a9a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 30 21:00:40 2017 -0700

    Fixed parsing problem with Kenosha County, WI (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyEParser.java
M	cadpage-private

[33mcommit 045d3a1e76a9bd3f1a80de91a6909c4d2b9d12c5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 30 19:11:54 2017 -0700

    Release 1.9.12-13

M	build.gradle
M	cadpage

[33mcommit bafd1271e0a51a0dda30b54a855c6ffa4d4c18a3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 30 10:51:45 2017 -0700

    Added Fairfield County, CT (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyParser.java
M	cadpage-private

[33mcommit 7ce32826c53026809080d303bc69c78a7c76d5a6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 30 10:13:32 2017 -0700

    Fixed parsing problem with Hays County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHaysCountyBParser.java
M	cadpage-private

[33mcommit de977268299c2e68f3c7bcad467d89b1910b73cd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 29 21:50:31 2017 -0700

    Fixed parsing problem with Talbot County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDTalbotCountyBParser.java
M	cadpage-private

[33mcommit 194fcc4c67b1583e2deb0a30b0f1e4f156ed3a64[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 29 10:28:47 2017 -0700

    Release v1.9.12-12

M	build.gradle
M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILJacksonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILRandolphCountyAParser.java
M	cadpage-private

[33mcommit 9354d45e62f62262d480f0ea54856bad79fa0fe3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 29 08:25:51 2017 -0700

    Fixed parsing problem with Talbot County, MD (added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDTalbotCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDTalbotCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDTalbotCountyParser.java
M	cadpage-private

[33mcommit e3a88d85532ee7386addf25c0df335977318df80[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 28 21:52:23 2017 -0700

    update A911 parser table

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICacombCountyParser.java
M	cadpage-private

[33mcommit 74130bfb84eaadafa777471e53e69d68497a633c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 28 14:01:17 2017 -0700

    Checking in Van Buren County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignCountyParser.java
M	cadpage-private

[33mcommit 0c0f8af0ed395d35e11ccb1891e7de8d088fdcbd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 28 13:19:32 2017 -0700

    Checking in Fort Knox, KY

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CORioBlancoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYFortKnoxParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA55Parser.java
M	cadpage-private

[33mcommit c07759e98bfa0b87257f48325090f98c83c3106b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 28 09:47:33 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAModocCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAColquittCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSPKParser.java
M	cadpage-private

[33mcommit 20a8bbdab1a471cd96c6e175056acc6d994144f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 28 08:31:25 2017 -0700

    Checking in Henderson County,KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHendersonCountyParser.java
M	cadpage-private

[33mcommit 07ea32592ae4409eabb1514cb943b7a7a1c87f74[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 20:02:10 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXJohnsonCountyAParser.java
M	cadpage-private

[33mcommit 2d657ff25e6cb404940774c9ac165072cd0fd3d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 19:21:31 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e6bc673ca69be668785306b1fda79e3cff5ce6de[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 18:38:16 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 609f666e8e0e4a89598701e38cad802f378b38c0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 18:37:51 2017 -0700

    Update sender filters

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCoffeeCountyParser.java
M	cadpage-private

[33mcommit 2d1f2fe6c5a26cc99c30008efc0e51633d32928d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 18:14:42 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMarshallCountyCParser.java
M	cadpage-private

[33mcommit d29d8861358545651aa8529dcfc72ea60c0b7040[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 17:17:46 2017 -0700

    Release v1.9.12-11

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGuilfordCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRowanCountyParser.java
M	cadpage-private

[33mcommit 9a0b235933536f96ce91370aa8350f009dd19b23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 09:05:33 2017 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyCParser.java

[33mcommit 58f4acc0a026d48a5e7ee4acc81d1a60f6ff4fab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 09:02:05 2017 -0700

    New District: Morris County, NJ (C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyParser.java
M	cadpage-private

[33mcommit 3c6e45fe98efcac2c95c53d1a0013dac045e835d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 08:36:32 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 8d9c5012b594325d88d1a626ef072052dcc8e010[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 27 08:32:07 2017 -0700

    Fixed parsing problem with Dothan, AL (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALDothanBParser.java
M	cadpage-private

[33mcommit efdff7a83b5036ab48a4680249ae35bc609a6113[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jun 27 02:43:20 2017 -0700

    general updates.

M	cadpage-private

[33mcommit d8164d9e09bd4fde1124fe6db6300aa3ac5a48ce[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 22:02:52 2017 -0700

    Fixed parsing problem with Cy Creek Comm Center, TX (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyCreekCommCenterAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyCreekCommCenterBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyCreekCommCenterParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyESD1BParser.java
M	cadpage-private

[33mcommit 1451b9b74e10630bc1045ded19a1491281b4c63d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 17:46:09 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCumberlandCountyParser.java
M	cadpage-private

[33mcommit 737397e1959a5dfe7548709f9316b3043885fdfc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 17:17:25 2017 -0700

    Fixed parsin gproblem with Davidson County, NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavidsonCountyBParser.java
M	cadpage-private

[33mcommit 471605af69c49cfb7e75f138b8613425aa0c6c03[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 14:43:14 2017 -0700

    Fixed parsing problem with Washington County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWashingtonCountyAParser.java
M	cadpage-private

[33mcommit 54cc0015643cc32fcf89302dce44f3092bc071d0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 14:31:59 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit efa788ddd844fc619209230bc24ad258acdf66bf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 14:19:38 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 361a7af31d5e7d8cf38b820511b355739ea7cb4b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 13:48:39 2017 -0700

    Fixed parsing problem with Pulaski County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPulaskiCountyParser.java
M	cadpage-private

[33mcommit 29d37df4db76a3083b5efefd9070f683cbf69e6e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 09:41:02 2017 -0700

    Release v1.9.12-10

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 7c1cc551e63fd0b7619bc3e3ed30c075c180d817[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 08:59:40 2017 -0700

    Fixed parsing problem with Muhlenberg County, KY

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMuhlenbergCountyParser.java
M	cadpage-private

[33mcommit bae679093f83e70fb45163071e3a4e06668015f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 08:20:54 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 4a374667617f3858f62b16fbcb738373543fbb53[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 26 07:53:09 2017 -0700

    Fixed parsing probelm with Dothan County, AL (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALDothanBParser.java
M	cadpage-private

[33mcommit 30e41c917e99fbeff0f9558186eec3059995289a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 25 00:21:58 2017 -0700

    Fixed parsing problem with Suffolk County, NY (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthamptonCountyParser.java
M	cadpage-private

[33mcommit 9d9a2a68c7d96ab81d6798647fd0750187def1b8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jun 24 01:49:54 2017 -0700

    general updates.

M	cadpage-private

[33mcommit beca3095e56a966309576694cde8320c607c0548[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 23 18:50:22 2017 -0700

    Fixed parsing problem with Campbell County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCampbellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
M	cadpage-private

[33mcommit 6f6b1708f791409ed43f53fc5664f75bcfe15f2c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 23 17:39:22 2017 -0700

    Fixed parsing problem with Williamson County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWilliamsonCountyParser.java
M	cadpage-private

[33mcommit 2fe078e9647cc2aa3d7cc2f4c1d190a5d37a8ecf[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jun 22 13:54:35 2017 -0700

    skeletons

M	cadpage-private

[33mcommit 0c18b4a494868af97c0dbc33bb7bae4c86115dfc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 22 08:12:18 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ba95c1df933a5e4932bd30be4428d82cd7f88270[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jun 22 02:11:30 2017 -0700

    general udpates.

M	cadpage-private

[33mcommit b101a2e99d8d6fd78127590288ca69e02cfc3697[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 21 20:06:15 2017 -0700

    Fixed mapping problem with Sacramento County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASacramentoCountyParser.java
M	cadpage-private

[33mcommit d1a910cc99d82eaa70f922f357ab6055b2bbb308[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 21 19:13:38 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyAParser.java
M	cadpage-private

[33mcommit e3677a615875ee7379e34e525e9d6df2091c86c3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 21 19:01:08 2017 -0700

    Fixed pasing problem with Franklin County, VA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit bf35d0b40dd0f6cb380d69d3f8223fd4a308d141[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 21 17:57:12 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 69b6ea288df60ef0f82c1774d13c1426d65b766f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 21 17:51:30 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit fec168de364091546876187532b7522412a30d71[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 21 09:31:51 2017 -0700

    Fixed parsing problem with Calaveras and Tuolumne Counties, CA (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CACalaverasCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CACalaverasCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CACalaverasCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATuolumneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATuolumneCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATuolumneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA69Parser.java
M	cadpage-private

[33mcommit b58afc4accee75c0f260dc5fcc33b04928f87518[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 20 19:36:21 2017 -0700

    Update msg doc

M	cadpage
M	cadpage-private

[33mcommit 129b9841fcac8a198904eca3ccb29d971cd7f8eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 20 18:46:31 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit aae48f2881e8add19a4aeb89a797ffb440798cf6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jun 20 02:03:36 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 311325d0c7663cb12c5fe95849ac9c65c1496ace[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 21:45:08 2017 -0700

    Fixed parsing problem with Cerro Gordo County, IA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IACerroGordoCountyParser.java
M	cadpage-private

[33mcommit 772627cc7c400dd7a1ab44c6449b85b9216258af[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 20:55:19 2017 -0700

    Fixed parsing problem with Benton County, AR (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARBentonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit b7324f8ca5465c3852f511d10dd4290001d33ae0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 19:36:41 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCANS/ZCANSAnnapolisCountyParser.java
M	cadpage-private

[33mcommit 7b2513ebf63ded9be3f45ba3caa33188eb0dcad4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 17:59:17 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 8aedcd38814c6c4e8dc693536c80f77d1e9813dd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 17:54:43 2017 -0700

    Fixed parsin gproblem with Boone County,  MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBooneCountyParser.java
M	cadpage-private

[33mcommit 87eb40e2a20bce3fd930a8af577dd40c6e59c8ef[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jun 19 14:42:26 2017 -0700

    general udpates.

M	cadpage-private
M	docs/support.txt

[33mcommit 323709478142ac1e3ddbdc08c2b8649d99dc2fec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 14:08:29 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ded8e0fd68c99a3bb5b60a06771e5d8b19e51b56[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 13:40:03 2017 -0700

    Fixed problem not being able to delete read messages
    when new message is added

M	cadpage

[33mcommit 17dbe57978a2055c1eed942106eef65adcbf4cfb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 11:07:30 2017 -0700

    Release v1.9.12-09

M	build.gradle
M	cadpage

[33mcommit 4d654f6c5bb60c9fb22c8ad14d48786e4e135fb9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 10:25:59 2017 -0700

    Fixed parsing problem in Annapolis County, NS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Message.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCANS/ZCANSAnnapolisCountyParser.java
M	cadpage-private

[33mcommit afb9aafa292fd4f0a1f083b0c470974f5793d69f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 19 09:28:46 2017 -0700

    Fixed parsing problem with Garrett County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDGarrettCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Message.java
M	cadpage-private

[33mcommit e337b44a4fdff3347d1476ccfcf9762d92e1148f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 18 13:27:04 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
M	cadpage-private

[33mcommit 76e5dae8089b28ab94c2858ac5b3f96277d0b918[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 18 12:57:13 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1b73ceb48ce65ce8f5de9c02aef76a82ca1575f2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 18 12:23:58 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 38f188466f1e3b3c9ae5839e407362ffab5f1a66[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 18 12:18:14 2017 -0700

    Update mgs doc

M	cadpage-private

[33mcommit cd6a0147ce6aa924d54ee701033410491625f3d3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 18 10:30:54 2017 -0700

    Fixed parsing problem with Clermont County (Added D)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyParser.java
M	cadpage-private

[33mcommit ef859cbd4d77fbd943f6c89eaabb398e342ac8ed[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 16 09:21:50 2017 -0700

    Fixed parsing problem with Tazewell County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILTazewellCountyParser.java
M	cadpage-private

[33mcommit 3ebbcf25800d09b9b3484522faed44f458da7b75[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 16 09:06:59 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit c2c5dd5b26495a4a1805cccc205c7d9cba39e706[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 15 20:37:01 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyCParser.java
M	cadpage-private

[33mcommit f4aafa699fbdbd1d79db0d2d2bc0172b6dafd3aa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 15 20:16:14 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 6ea7cf0cccf3899b0bc5a1936f2ae63e0622245d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 15 19:36:23 2017 -0700

    Fixed parsing problem with VAGalax (added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGalaxAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGalaxBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGalaxParser.java
M	cadpage-private

[33mcommit dd08516469eba7eae8d51c0993d516a4182a8c70[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Jun 14 23:03:45 2017 -0700

    general updates.

M	cadpage-private

[33mcommit e3f59f043a3b2a56bdf83c65bf71b21500468c34[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 14 21:57:25 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORTillamookCountyParser.java
M	cadpage-private

[33mcommit 10e1a74102a87c46b790e8008b6998c435ffa9eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 14 20:12:44 2017 -0700

    Fixed parsing issue with Ashe County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAsheCountyParser.java
M	cadpage-private

[33mcommit f225e0d00802e529bec4687f54bb3e93009bbf35[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 14 19:52:21 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 3c627857aa1a3ee5c6b83c613cf0a710f4e023d6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 14 19:05:38 2017 -0700

    Parsing problem with Oakland County, MI (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyCParser.java
M	cadpage-private

[33mcommit 26ec5c1890c92ec2339eca3c938bcf7ed5f40edd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 14 18:26:45 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e5cb39a685cf60f485973adc51350be41d134ae3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 14 00:24:18 2017 -0700

    Release v1.9.12-08

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOColeCountyParser.java
M	cadpage-private

[33mcommit c2dc0322398f999d7d6f5775c06695293aed548f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 13 22:06:32 2017 -0700

    Merge

M	cadpage-private

[33mcommit 7ca8cb0e96a442c81fd628ced710821a8ef274ca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 13 22:04:58 2017 -0700

    Fixed parsing problem with Clermont County, OH (Added C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA52Parser.java
M	cadpage-private

[33mcommit 9460d1d74e7147ff9ad2408e51641d5443b0fbc5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jun 13 19:19:47 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 8f09629da2854c1d202a0ef0e5352580a269156f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jun 13 18:08:27 2017 -0700

    completed parsers for
    WVGrantCounty, MOCamdenCounty, MNRedwoodCounty, INGibsonCounty, MOMariesCounty, INNobleCounty, COPitkinCounty, MOCaldwellCounty, MNRoseauCounty and MOWrightCounty.

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INNobleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 5cef4d0194ec060e1335758af752bd25c429ea7f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jun 13 16:08:21 2017 -0700

    general updates.

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 81e868f0226a3c63f1f93e1cd0a8d14cdfd2e080[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 13 15:07:45 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 12a456ed605837b738b14bb2d50ae9d4c5fea63c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 13 14:53:45 2017 -0700

    Fixed parsing problem with Monterey County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
M	cadpage-private

[33mcommit 3862735660b4d11cd4a0358590f5ac236fbde9f8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jun 12 20:32:34 2017 -0700

    general updates.

M	cadpage-private

[33mcommit f786c45eabd3f634c0889d6db7c29689f648e677[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Jun 11 22:25:48 2017 -0700

    general updates.

M	cadpage-private

[33mcommit f786176393d5182451b3dcda4aeff0a712828fc0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 11 21:53:45 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit e6a91b00350ffd3af0533b09e0f4f05b89d5cd58[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 11 21:37:22 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 0e13b25d4e48962ec077a2299de49efe497c7b44[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 11 21:14:33 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 8c4aae9bdec64ef21b94a0f5394b598c4bdf7f7f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 11 21:01:14 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyNParser.java
M	cadpage-private

[33mcommit 14ab84438e3d0fad40ec1ffe57aa961255ac71e7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 11 20:44:52 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOColeCountyParser.java
M	cadpage-private

[33mcommit 78c36986a6f479e4001644981cf0915562ed7b10[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Jun 10 23:27:09 2017 -0700

    general updates.

M	cadpage-private

[33mcommit a826095d8f33ef11b8d024745e53726dc275f607[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 10 08:56:47 2017 -0700

    Added call code lookup for Medina County, OH(B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyBParser.java
M	cadpage-private

[33mcommit 531882efdfb9cd47a8dc16ae630fcfb894dee01d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 10 08:48:45 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit c08f2bd40e21efb28d99f9ee67d303d3cc8e4785[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 10 08:31:14 2017 -0700

    Cleaned stuff up

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMarionCountyParser.java
M	cadpage-private

[33mcommit 70b2cdd3e370866bd52f8194ee150c60a3188b16[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 10 07:53:31 2017 -0700

    Fixed parsing problem with Marshall County,KY (Added C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMarshallCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMarshallCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA48Parser.java
M	cadpage-private

[33mcommit 93f83c9fbbd070252cefd271186a21c5e883b2bc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 9 21:44:57 2017 -0700

    Update genome.log

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-private

[33mcommit 296dfe08fbbaa7c33de0d3d801d8fff55c3ed3e8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 9 20:22:30 2017 -0700

    Fixed parsing problem with Ellis County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA55Parser.java
M	cadpage-private

[33mcommit 76840880192417d073bce47f1b974eae2b7c0398[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 9 18:42:45 2017 -0700

    Fixed parsing problem with Montgomery County, OH (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyAParser.java
M	cadpage-private

[33mcommit 79b8c963fd3eb466cee7652e797216ae13b28be1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 9 10:14:14 2017 -0700

    Release v1.9.12-07

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 16ea7a3f2d1fd7d9bf9b77fb5652568eb273811b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 9 09:33:46 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 07f696a2b989f4286d1e08d1b0ac54d0a71aec72[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 9 09:19:28 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit a3d32b723bb904ef4f4dbab26509258c5b80e426[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 9 09:03:55 2017 -0700

    Fixed parsing problem with Whitfield County, GA (Added D)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWhitfieldCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWhitfieldCountyParser.java
M	cadpage-private

[33mcommit 3f894a716e6e5b41bfb6645007c1729ae987809b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jun 9 00:13:46 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 69873e7fc785795fa11cb98dcb3679b5da332f9a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 8 21:59:31 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyCParser.java
M	cadpage-private

[33mcommit 21f997d41c1dda008c395cc10b792f61ab17854b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 8 20:29:21 2017 -0700

    Fixed parsing problem with Morris County, NJ

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyAParser.java
M	cadpage-private

[33mcommit 8bfcf6a524ec8eb96b75f0cd5a80d5f9706ff815[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jun 8 15:55:10 2017 -0700

    workload

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNFranklinCountyParser.java
M	cadpage-private

[33mcommit f08f6a5b088ee6643190f72fe50c5463834a1352[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 8 06:51:31 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyBParser.java
M	cadpage-private

[33mcommit c8895641f982737764931f328dbd9338a12b30da[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 8 06:37:14 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit d000a1c33cded6511d44a4ff5fcc7883f17458c9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 8 06:00:37 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBroomeCountyParser.java
M	cadpage-private

[33mcommit 4a4f0b253c8042ccf59fb5e253f81b6ed831f726[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 7 19:37:47 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 4c0f8d2cb36900bb9e272deeffe7a6a26d0e5d64[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 7 11:38:29 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit cace459f59e8064178f33548915d80c59ba441f0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 7 11:30:20 2017 -0700

    Fixed parsing problem with Lamont, Lacomb, Clearwater, Strathcona, and Red Deer Counties, AB

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA51Parser.java
M	cadpage-private

[33mcommit 4510cee3c964670c846e0f763117e6e469c5d670[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 7 08:10:59 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 6396b4afb9eb73801557669e497c74cde52f934f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jun 7 07:56:49 2017 -0700

    Fixed parsing problem with Mahoning County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyAParser.java
M	cadpage-private

[33mcommit 94021a57f1f6218459dafc21175afcd8679bc804[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 6 23:13:32 2017 -0700

    Release v1.9.12-06

M	build.gradle
M	cadpage

[33mcommit 7b6b46be057353fb16fcacf1f3745843db22a9fe[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jun 6 22:54:47 2017 -0700

    general updates.

M	cadpage-private

[33mcommit bff0725c2fca55c5ad2ef0711411244f2ed257ae[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 6 22:49:19 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 672de8b28a95531429faf694cbf05b0fcf5cbd2f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 6 22:42:25 2017 -0700

    Fixed tests

M	cadpage-private

[33mcommit 1bcf7faa20ca549ffb598a8103e42a5e904c0118[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 6 22:24:29 2017 -0700

    Fixed parsing problem with Medina County, OH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyAParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit a386b964a5267592f1c2358c57b2d866f3f52ba7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 6 17:47:50 2017 -0700

    New locatio Medina County, OH (B & C)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyParser.java
M	cadpage-private

[33mcommit e28101556e96240f4914d9f98292f4f754958d0f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 6 17:34:20 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1ee8f32c77e6d8b205b21392a348c57707815e6b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jun 6 10:25:27 2017 -0700

    Release v1.9.12-05

M	build.gradle
M	cadpage

[33mcommit f34156086c6e55ba4318ee313be5308ac1f82e98[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jun 5 22:09:21 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 4b94256a6eb45ab0d876da3f65562e213f91ed53[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 21:50:38 2017 -0700

    Added Jackson County, FL location

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLBayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLJacksonCountyParser.java
M	cadpage-private

[33mcommit c36826a6e1f4dbafeef1de5158d91239cc709c7c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 21:01:14 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 5c2ce48e843d56f1ee1fa872f2c41c07a5b765ea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 19:21:36 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCaswellCountyParser.java
M	cadpage-private

[33mcommit ecc1ca82664cc0382572a5807e44ead5fc6ab369[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 19:07:16 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 176fb909ebd26765284248c5eb84773e3bea1014[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 18:30:49 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 145f3ff31fea6e14d878d70ec174564507ba45fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 18:19:21 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyAParser.java
M	cadpage-private

[33mcommit bd79ac19251026dfd57b69520c818cb67551d3c8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 18:05:57 2017 -0700

    Fixed parsing problem with Cayuga County, NY (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyAParser.java
M	cadpage-private

[33mcommit e3bfb21652b06177655f841a716f39a0c90cc036[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 15:39:43 2017 -0700

    Checking in Champaign County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignCountyParser.java
M	cadpage-private

[33mcommit 84c286275b898d852a1e6ec2a8749fad8eb5b4f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 08:38:48 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 33510a32d6c7e174333a9c22efdab59a81e7f573[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 08:30:13 2017 -0700

    Release v1.9.12-04

M	build.gradle
M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNRheaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNRheaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNRheaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchGeoconxParser.java
M	cadpage-private

[33mcommit 80723ff5282f019f14ed1c7cdb3dc12dd1c58f3b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jun 5 07:21:05 2017 -0700

    UKpdated A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 0bc6593bbd8e9d4e376db1d3080c1ba7f8b06070[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Jun 4 23:36:26 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 2b858e60a704185d6dd6e97f08b34306f224861d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 19:08:16 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 6a6e11c344e8be5108a344e831bf17cafe5a6aef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 18:48:35 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit b5d60ab463f6cf60a05dcdb369a14ea342a7abb2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 18:47:35 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYJohnsonCountyParser.java
M	cadpage-private

[33mcommit f16b98f82d2506dccf236f42b219fc93d1b6603d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 18:35:48 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit b598197eb5588f74b888266eb45f7182a9476fcc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 18:19:59 2017 -0700

    fixed parsing problemw ith Teller County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COTellerCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASnyderCountyParser.java
M	cadpage-private

[33mcommit 58d324d0dd2f8f466b2351394a6e9ade99c7e80e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 17:33:37 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCarteretCountyParser.java
M	cadpage-private

[33mcommit fe8f3d852a3d0940a292e9bea9dde7b1209f0359[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 17:20:49 2017 -0700

    Fixed parsing problem with Rhea County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchGeoconxParser.java
M	cadpage-private

[33mcommit 111e96583b6bb24d3a8ac0f0f1eb80bbda40fac7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 16:52:31 2017 -0700

    Fixed parsing problem with Suffolk County, NY (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAParser.java
M	cadpage-private

[33mcommit 1f3e41c9bbb0f58928749c1b6753e3793f9ff9df[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Jun 4 16:38:36 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit ad55b52dd154aca4c9c3adb7ad8e66b0e0e85532[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jun 4 15:32:13 2017 -0700

    Fixed parsing problem with Preble County, OH (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyParser.java
M	cadpage-private

[33mcommit acb2d9bf709dd7a4f930ba574202992d123b8f1d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jun 3 07:47:58 2017 -0700

    Release 1.9.12-03

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyDParser.java
M	cadpage-private

[33mcommit 5aa2a6693e5823023cb2842c670d899e5734350e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 2 09:51:39 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 5d3c46c5f578335640f642eb5fbbe0a7a67b0834[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jun 2 09:51:20 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ef0c017739ba2449b30e68931dd52e4d3d852620[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 1 19:58:46 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 01df4cbcddf3d2b20416ae83d4eb978a5aa5c3d6[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jun 1 18:43:42 2017 -0700

    last ticket, sortof

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CORioBlancoCountyParser.java
M	cadpage-private

[33mcommit 16bf86e89fb141495bc8223a2ec524c5093be4cd[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jun 1 17:05:04 2017 -0700

    all the things

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAModocCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAColquittCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOScottCountyParser.java
M	cadpage-private

[33mcommit 6ff12d1f2f0500628e10945bf7276bdbc54d0f69[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jun 1 12:18:05 2017 -0700

    Update everything again

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignCountyParser.java

[33mcommit 54355c01bf3ead0c8e93ec330d713becade14cb1[m
Merge: 675fc40 d03daad
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jun 1 12:17:31 2017 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 675fc40f88585ac50fc22b44c5185b6b9b53623b[m
Merge: 1e460ca 189eff0
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jun 1 12:16:53 2017 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers
    
    Conflicts:
    	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignCountyParser.java

[33mcommit d03daad682ec8f1c40e779680a46d6badd40bfbe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 1 10:56:48 2017 -0700

    Fixed parsing probelm with Charleston County, SC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyBParser.java
M	cadpage-private

[33mcommit adbea5554d36afce195acc842b39b9f853bb3977[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jun 1 10:06:54 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 36b28510d1c8a509da7ab3fb7ac8d6ccc47be3f9[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 31 22:47:43 2017 -0700

    general updates.

M	cadpage-private

[33mcommit a62a84c70e7d523d7d295560c55c8c3a8c861e94[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 31 14:26:20 2017 -0700

    Release v1.9.12-02

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALShelbyCountyParser.java
M	cadpage-private

[33mcommit 728d94766f41f02c3761be9f625ceaea09b8fc32[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 31 12:57:36 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit d0e637860da8676748c17108aba854dc7eeaacf8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 31 12:47:58 2017 -0700

    Update more stuff

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCaldwellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWataugaCountyParser.java
M	cadpage-private

[33mcommit 36c5a5611556838c18b97d0ef303e4f7781b5628[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 31 12:09:50 2017 -0700

    Parsing problems with Watauga County, NC

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWataugaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 330c6e4184857fdb36c981eac453b59932861578[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 30 22:48:27 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit f983b89f17422093320c2e7c06916e83deab1ac2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 30 21:15:19 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ee9d52fa24bd0f321030097865168c19f83f4a6e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 29 19:56:38 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 7bb07825b959e91930c33cccc0670246f6b71f3d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 29 19:16:50 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit afcb265b9a0c04b4e7cef8cf560b1c1fe4ad0681[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 29 18:43:52 2017 -0700

    Fixed parsing problem with Mercer County, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMercerCountyAParser.java
M	cadpage-private

[33mcommit d39e8ab6583382e429a7e386a8a281ce3326ed4f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 29 09:45:08 2017 -0700

    Moved SMS message processing to worker thread

M	cadpage

[33mcommit c6a48a5cc88a2a55384666ab2fa923effeef7f77[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun May 28 23:48:21 2017 -0700

    general updates.

M	cadpage-private

[33mcommit ca2f55e30a7acf0d53f3b75d061332413065c83c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 28 10:07:05 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 63bfe4610185099450e74dfcd188eec0e39e6268[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 28 09:53:39 2017 -0700

    Fixed parsing problem with Greene County and Christian County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOChristianCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA52Parser.java
M	cadpage-private

[33mcommit e689e42d331fd4a23857e70865e65be44def50ac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 28 07:35:32 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 08df4d831e9f8af1b9d0e8a67ef882d84c706565[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat May 27 23:41:24 2017 -0700

    general updates

M	cadpage-private

[33mcommit 20274fd2c8884071ba0325e8d19e7c63b6cb2c41[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 27 22:26:22 2017 -0700

    Update msg doc

M	cadpage-private
M	docs/support.txt

[33mcommit 068f6ed4cca83b85fdf306e23033375834a4246e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 27 20:41:15 2017 -0700

    Fixed parsing problem witih Franklin County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyBParser.java
M	cadpage-private

[33mcommit c72d1489fe370a39a80dbbaff819f4810eaaa4dd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 26 21:12:35 2017 -0700

    release v1.4.12-01

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit d4012e770292a3d8da73d2ba1e6870bc4dcefec4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 26 09:22:50 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyCParser.java
M	cadpage-private

[33mcommit bc2d71b1c03cc43d27bf72513d7b833a195034d1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 26 09:17:36 2017 -0700

    Fixed minor parsing problem with Pierce County, WA (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit d56c53fc64fb2b3585d9f92fc10dce8692dd1f55[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 26 07:50:00 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 5478fb8f30a8e4d7fe1a159d904cbc8946778290[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 26 07:37:32 2017 -0700

    Fixed parsing problem with Tulare County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATulareCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADauphinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA49Parser.java
M	cadpage-private

[33mcommit 7f7524709e4138b5c38237d57b6840bd04f4fdd8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 25 22:36:05 2017 -0700

    Fixed parsing problem with Dauphin County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADauphinCountyParser.java
M	cadpage-private

[33mcommit 78717de25c46b9386647bd34c026aff06b2e8854[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 25 19:19:34 2017 -0700

    Fixed test problems

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 1e460ca66a24eab69038a678fe535a56ba717930[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu May 25 14:05:18 2017 -0700

    test

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHendersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSPearlRiverCountyParser.java
M	cadpage-private

[33mcommit 189eff01b1bc6a9d3ef6c1b024e023f70d478263[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 25 11:30:14 2017 -0700

    Fixed parsing problem with Linn County, OR (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit 9d9c887f4135255f5f01e3f7eaa09347beb60efb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu May 25 02:52:07 2017 -0700

    general updates.

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTAddisonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTAddisonCountyParser.java
M	cadpage-private

[33mcommit e4b655643a0d040c783b0da08cb86bfbb675c8bc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 24 09:17:05 2017 -0700

    Fixed parsing problem with Kenosha County, WI (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyCParser.java
M	cadpage-private

[33mcommit 7bc143638c2e092c3f9b815b9825d089af1f2474[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 23 22:40:53 2017 -0700

    Fixed parsing problem with Trenton, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHTrentonParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit a9c7398c29036d43d536d03d6fe52d98a86c82fd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 23 18:29:23 2017 -0700

    Fixed parsing problem with Baldwin County, AL

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBaldwinCountyParser.java
M	cadpage-private

[33mcommit 7a8186c9440ca13d5bce816a2f6041db674b4551[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 23 11:29:59 2017 -0700

    Fixed parsing probleml with Linn County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit 9a49bc491a987b5937ad9b58480b0a5d15b38c53[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 23 08:04:08 2017 -0700

    Fixed parsing problem with Pierce County, WA (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit b808f33acddd37a203e7a0084ee8b244b8aca78a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue May 23 01:57:05 2017 -0700

    general updates.

M	cadpage-private

[33mcommit cc7cb6988ee379ebbb31fd5623eadbfb026095df[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 22 22:47:41 2017 -0700

    Update msg doc

M	cadpage
M	cadpage-private

[33mcommit 4f1d6c75fdcaccf530b00e6927aa5af6048dcf28[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 22 21:33:00 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 2839a9b5ae271b957b232ac02087b30a70ade840[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 22 21:16:30 2017 -0700

    Fixed problems with 3 line history update

M	cadpage

[33mcommit 45fe3c3b30fd65968e39063d030a104bcc8f0ae5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 22 19:58:51 2017 -0700

    3 line history option

M	build.gradle
M	cadpage

[33mcommit 810599934fb825eb8e35e30c0b9ec05c5d475ace[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun May 21 02:37:40 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 6e3145aa6684a16a87c62f4d367ef327a4ab8b23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 20 20:20:26 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit 70c1f5e365cd1b246858200241666fd812976c44[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 20 20:11:28 2017 -0700

    Checking in Champaign County, IL

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignCountyParser.java
M	cadpage-private

[33mcommit e01c086701ed399ce9861c993f6da1b099df2bd8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 20 12:31:16 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 59e5495d55807c934b28ccf52386ae20c03a2f7b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 20 12:02:00 2017 -0700

    Fixed parsing problem with Lackawanna County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-private

[33mcommit 61f85fdfe4d0a52d089e31c96a17d501557f4520[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 20 09:36:56 2017 -0700

    Fixed parsing problem with Clinton County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClintonCountyParser.java
M	cadpage-private

[33mcommit b4caf486abf408f0614fad522dddcd4c9a2eb23b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 19 20:55:45 2017 -0700

    Fixed parsing problem with Minneapolis/St Paul, MN (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMinneapolisStPaulBParser.java
M	cadpage-private

[33mcommit 7cb2da7a6f90e09f731000e04ef67f474fb6aaf7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 19 19:15:52 2017 -0700

    Fixed parsing problem with Hays County, TX (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHaysCountyBParser.java
M	cadpage-private

[33mcommit bda87bc7f6baee2be31d8de32e1d239bed6a67d7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 18 20:51:16 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 8bce5a08601422cff426c821b25ae01a84001941[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 18 20:34:55 2017 -0700

    Fixed parsing problem with Mercer County, OH (added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMercerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMercerCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMercerCountyParser.java
M	cadpage-private

[33mcommit 0e8c0e410c31709bfe78b28b869b50dbf19bfea5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 18 11:51:31 2017 -0700

    Release 1.9.11-50

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA1Parser.java
M	cadpage-private

[33mcommit 4175e72e1703339a8dce104e8f600870bf4d6b9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 18 08:17:26 2017 -0700

    Update Active911 URLs

M	cadpage
M	cadpage-private

[33mcommit ab717619606ea389d9fdb53b94d24db03a05b906[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 22:23:03 2017 -0700

    update genome.log

M	cadpage-private

[33mcommit 66e2f969d7342005b2220dca302119a9a8a99c03[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 22:22:19 2017 -0700

    Fixed parasing probelm with Delaware County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA1Parser.java
M	cadpage-private

[33mcommit ad36733f5149f42f26839e817955ebcf04dc6732[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 22:11:53 2017 -0700

    Fixed parsing probelm with Sevier County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSevierCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 1a26ebdd12e4d153a21ad7709f016a09f8d9abb7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 19:52:44 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 2ce0a9fd0cabc39636b5929b91f1d89b55648700[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 19:42:04 2017 -0700

    Update sender filter for Robeson County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRobesonCountyParser.java
M	cadpage-private

[33mcommit eb1c3914e405f3721ad702447a2d1e0bd3808a4e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 19:30:39 2017 -0700

    Fixed parsing problem with Centre County, PA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java
M	cadpage-private

[33mcommit 76d2e2c60538ba3bf92d048ee4acb97307715b58[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 17:15:42 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 91294b416ecf592ac1aa7938043e4d568e215a25[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 17:10:29 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyCParser.java
M	cadpage-private

[33mcommit 7e4a75724cd2df4b50f60dfce022aa3e7be519ce[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 17 16:22:05 2017 -0700

    Fixed parsing problem with Kenosha County, WI

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyParser.java
M	cadpage-private

[33mcommit 5bc87036b33c039007d504986ff0d04b5997ffbd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 17 01:36:18 2017 -0700

    general updates

M	cadpage-private

[33mcommit 52487a0679277e4b268e03132af1de94bdcad000[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 16 19:28:49 2017 -0700

    update genome.log

M	cadpage-private

[33mcommit ad5ecf6f72fa781c20696fe9c37a9e73f0ddfcda[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 16 18:35:02 2017 -0700

    Fixed parsing problem with Chatham Kent, Ontario

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONChathamKentParser.java
M	cadpage-private

[33mcommit cf7b202928e4e25a647c576bd810af90bc730dc5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 16 08:04:48 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit 74bd69df340c25e0eef856bde3b482fa8093b555[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 15 22:02:26 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 299445de793ee0ba4460e0178e8f0589822010af[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 15 21:38:51 2017 -0700

    Fixed minor parsing problem with Pitt County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyBParser.java
M	cadpage-private

[33mcommit 795bfb73237f59bd26453a3522a35d0d8e9e43b9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 15 20:39:29 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ba38297dd0ed5db9a92f50bd82d419464c14ad95[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 15 20:25:08 2017 -0700

    Added Wake COunty, NC (C)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRobesonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWakeCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWakeCountyParser.java
M	cadpage-private

[33mcommit 7729b9086c3288898eb66b142014f196660679d4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 15 13:38:42 2017 -0700

    Update msg doc

M	cadpage
M	cadpage-private

[33mcommit 490ee7a27405159ffe185aa5da325a68cdf2a74c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 15 11:47:01 2017 -0700

    Added Sebastian County, AR

M	build.gradle
M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARSebastionCountyParser.java
M	cadpage-private

[33mcommit 28da1d0d1238df704d184fbfc01171e1c76aa372[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 14 17:32:17 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCColumbusCountyParser.java
M	cadpage-private

[33mcommit 6d904ac8b8110adca3b5f444dd59b40fe48050f8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 14 16:59:20 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 8e731ca3d98157853ec2ca011756afd28f6b8a9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 14 16:41:48 2017 -0700

    Update Broomfield, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBroomfieldCountyParser.java
M	cadpage-private

[33mcommit 50727b987cdaf8cadaeba89c9f359cc9a65fe046[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 14 14:44:40 2017 -0700

    Fixed parsing problem with Kenosha County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
M	cadpage-private

[33mcommit fab70c857346cb5880c41fd7bb2545390a09315f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 14 13:38:34 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 06dbf6fcdcf34174860f214dff452edb11b85a41[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 14 11:22:58 2017 -0700

    Update sender filte for Gibson County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INGibsonCountyParser.java
M	cadpage-private

[33mcommit c76ed8b4b5cbaa63c3974801e8da48fa3c8c21c0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun May 14 09:45:24 2017 -0700

    Fixed parsing problem wth Lancaster County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit 79c1aec61d71a665ea83d884d66ff9dcda7da7b3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun May 14 01:13:25 2017 -0700

    general updates.

M	cadpage-private

[33mcommit d7f9dd9abb8b5d6ae154787d35c3808e2d0e9520[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 23:41:11 2017 -0700

    Fixed parsing problem with Gallatin County, MT (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTGallatinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTGallatinCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTGallatinCountyParser.java
M	cadpage-private

[33mcommit f03d40717a22ed5e88f72d57756e6d469777c36a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 20:53:19 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 728f287f6ab9221dac5da5bfdff8f9e53c7cb508[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 17:22:08 2017 -0700

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWashingtonCountyParser.java
M	cadpage-private

[33mcommit e5d4121a5555fecbc3602203d160380db9dae0fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 16:47:15 2017 -0700

    Fixed parsing problem with Madison County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMadisonCountyParser.java
M	cadpage-private

[33mcommit b2e2497b4bcf59081fd329a940a1eca1d8fd06fa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 14:28:42 2017 -0700

    Fixed mapping tables for Murray County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMurrayCountyParser.java
M	cadpage-private

[33mcommit 61bf5fbc89ea367746db57bc44c942d3472a091d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 11:39:50 2017 -0700

    Release 1.9.11-48

M	build.gradle
M	cadpage

[33mcommit 46e6120dc0403564a81fdc8358891bfc8200782c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 10:48:27 2017 -0700

    Fixed another problem with Kenosha COunty, WI (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyDParser.java
M	cadpage-private

[33mcommit 3496c1c46ad57540c55c222b53e9bc4acde87970[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat May 13 10:32:07 2017 -0700

    Fixed parsing problem with Kendall County, TX (Added B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKendallCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKendallCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKendallCountyParser.java
M	cadpage-private

[33mcommit 49733084ce8d67ed09b3e4a32c837eaf53b5c181[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri May 12 23:32:41 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 583aa08e9bb199992368a4dded5e5222ff343243[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 12 20:10:59 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 31505b4622bda9fe4044f7dcdd5eb71fa46d2cbc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 12 20:00:14 2017 -0700

    Fixed parsing problem with Anoka County, MN (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNAnokaCountyBParser.java
M	cadpage-private

[33mcommit 77ad6c2ca7dec1c0afd098a150530a9b80ad56ec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 12 18:50:09 2017 -0700

    FIxed parsing problem with Montcalm County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMontcalmCountyParser.java
M	cadpage-private

[33mcommit 8a1acad6a4eec926292bb159c4321f9e4a47eca0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 12 17:58:11 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit b4932aa9cc671faa9e289a3abeacb8a45cdaff54[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 12 16:58:41 2017 -0700

    Fixed parsing problem with Kenosha County, WI (Add D)
    and Racine County, WI  (Add C)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIRacineCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIRacineCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
M	cadpage-private

[33mcommit 481bebd61067785c19bb8eaa4308759a81a442fd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 12 11:16:29 2017 -0700

    Fixed parsing probleml with Columbus County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCColumbusCountyParser.java
M	cadpage-private

[33mcommit e94bf16151141e0ae244ccb96d4b73f0d6599e9d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri May 12 02:37:24 2017 -0700

    general updates.

M	cadpage-private

[33mcommit fae5ecc53b4e485650a70a8fff895d59ddfc2075[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 11 21:12:57 2017 -0700

    Fixed parsing problem with Jackson County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAJacksonCountyParser.java
M	cadpage-private

[33mcommit f883bbf97fc93336ddb161115933c86251fad216[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 11 20:38:55 2017 -0700

    Fixed parsing problem with Jefferson County, TN

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNJeffersonCountyParser.java
M	cadpage-private

[33mcommit c3ed8a913d508c9bd0f25ce2054ad4b941dbd368[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 11 20:08:12 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 4d342ceedf8bb5af46888cd9ede06735e600882b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu May 11 16:13:30 2017 -0700

    gerneral updates.

M	cadpage-private

[33mcommit 043fa983c47d599e4424046a33b5934ba3874c43[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 11 10:10:28 2017 -0700

    Updage msg doc

M	cadpage-private

[33mcommit c68d4931841deb2d341d419dab5381b1df5600f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu May 11 10:01:28 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit fcc459d84cdb045631783831c22f14874ef9a962[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 22:20:41 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 8edebdd9acc2b45907f8e9bc1d1de4141715f347[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 21:13:49 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit b218d18ea873f2ecf6af8064119414d5c0c9b4e2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 20:50:47 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ebf2ee58731fcb25eb5848e32734e8a0612d2dd1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 20:31:28 2017 -0700

    Fixed parsing problem with Franklin County, PA (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyBParser.java
M	cadpage-private

[33mcommit f5ae74f93af5d8721084edbbf2b3f4bea7e58e70[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 20:11:09 2017 -0700

    Fixed parsing problem with Larimer County, CO (D)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyDParser.java
M	cadpage-private

[33mcommit 4083cf11e49ee4ff76b87ff940a108eb4e3456f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 19:53:57 2017 -0700

    Fixed parsing problem with Hamilton County, IN (D)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyDParser.java
M	cadpage-private

[33mcommit 242fdf255f5e0d7b90c89831fb212e31f6e18174[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 18:43:24 2017 -0700

    Fixed parsing problem with Grafton County, NH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDFrederickCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA32Parser.java
M	cadpage-private

[33mcommit af205d02dfaad3ff98e0118cdab7c4f56118e9b8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 17:14:59 2017 -0700

    Added Marshall County, KY (B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMarshallCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMarshallCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMarshallCountyParser.java
M	cadpage-private

[33mcommit fd10a187d9289e56370f50577bc38c52799772f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 16:12:14 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 2b07a12d80f0c00cb990d3cdaba88d431e3be962[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 16:01:01 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyAParser.java
M	cadpage-private

[33mcommit fbe2e5e7e454303adac2c5035a2bebc7d486f263[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 15:47:29 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ca257e968e205b59c08044c55efe3380c4f0902b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 15:34:01 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 8c3c8f347cb8dc6226da4250259d03730a33113d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 14:34:25 2017 -0700

    Fixed parsing problems with Fayette County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-private

[33mcommit b0489c121a080af3ba86242f2409ad488a340c1b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 13:59:46 2017 -0700

    Fixed parsing probelm with Broward County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLBrowardCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchPrintrakParser.java
M	cadpage-private

[33mcommit fc798890f8b871172c0f122a4083952fd5658e79[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 10:55:03 2017 -0700

    Updated Franklin County, PA parsers

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLBrowardCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyBParser.java
M	cadpage-private

[33mcommit 350da28d449e228daa94971d1c76a37dcab8c0cc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 09:24:33 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit c4890c41c32eaca0d13954b19e028cceac13f9e6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 10 06:24:10 2017 -0700

    update genome.log

M	cadpage-private

[33mcommit 67a0de6cc7e071f8659358ad5861529c0b76a619[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 9 20:48:13 2017 -0700

    Update sender filter for Marion County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA14Parser.java
M	cadpage-private

[33mcommit 7c80be79e0343284f903cc4b81e31a042b1adb3b[m
Merge: 23c20f0 2efdad1
Author: Jean Goul <jean@cadpage.org>
Date:   Tue May 9 20:38:58 2017 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 2efdad19dce352b9e0fbc3d87fc7182a3a77361f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 9 20:19:54 2017 -0700

    Release 1.9.11-47

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit de58d421dfe2478bbb1e3ea4dd496a53e04e7941[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 9 11:48:33 2017 -0700

    Fixed parsing problem with Washington County, VA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWashingtonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 757b4e303c98ecea460a48027d8f444cb2a9b44d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 9 09:24:34 2017 -0700

    Fixed parsing problem with Geneva County, AL (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALGenevaCountyBParser.java
M	cadpage-private

[33mcommit 617296440c7f04a76210c53a65e8f2dd1b208c64[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 9 08:20:08 2017 -0700

    fixed tests

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchProQAParser.java
M	cadpage-private

[33mcommit 856733256f48f6cdde798b94fbf4005c086fa25e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 9 08:08:58 2017 -0700

    Checking in Swedish translation updates

M	cadpage

[33mcommit 23c20f015d2242d63fe744c61ec563cd5d608614[m
Merge: 8295ab1 ad1594e
Author: Jean Goul <jean@cadpage.org>
Date:   Mon May 8 23:02:29 2017 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit ad1594e37b674e3ff1b96b67a7e47051c89d7516[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 8 18:14:20 2017 -0700

    Added logic to recover from Active911 app upgrade

M	cadpage
M	cadpage-private

[33mcommit 8295ab16f8c29fefd94af1d6faffd239ba29e095[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon May 8 15:50:02 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 5e49fad31225500e9a9224c474acfb6810b87602[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 8 13:12:10 2017 -0700

    fixed parsing problem with Johnson COunty, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSJohnsonCountyParser.java
M	cadpage-private

[33mcommit 0a8f7a841fb8ab53ce8d86ec259aa823e57fa13b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 8 12:22:29 2017 -0700

    Update docs

M	cadpage-private
M	docs/GCMProtocol.txt
M	docs/support.txt

[33mcommit 5fea51f9113988012be570c56e6799046d46c7ef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri May 5 21:49:18 2017 -0700

    Modified to require account permissions before registering with
    Cadpage Paging Service

M	cadpage

[33mcommit 16cbd59e22594ad621670771ac17ccef1ff3d83f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri May 5 03:49:48 2017 -0700

    general updates

M	cadpage-private

[33mcommit 01d0dd6bb880e33e1c32bfadfed53607404adc81[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu May 4 15:58:47 2017 -0700

    spooky scary skeletons

M	cadpage-private

[33mcommit e8de4f98d01e044376fe7011bd19add53cd44c7d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 3 16:18:10 2017 -0700

    gen updates

M	cadpage-private

[33mcommit d3793fb89d9f9681649b6a4644207404d7b55e70[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 3 11:03:26 2017 -0700

    Release 1.9.11-46

M	build.gradle
M	cadpage

[33mcommit 5b5c660f56573972c44253105e64d62e599be5ce[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed May 3 08:09:40 2017 -0700

    Fixed parsing problem with Nassau County, NY (M)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyMParser.java
M	cadpage-private

[33mcommit 07626b2805e55399441de75bab9e19864d3c8fb7[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed May 3 03:03:46 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 88ab54bc127e368f3c42d7369c390c072642902a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 2 18:22:54 2017 -0700

    Release 1.9.11-45

M	build.gradle
M	cadpage

[33mcommit 90e2247d85342092a6c9d6346a8ca496b462bbff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 2 08:59:01 2017 -0700

    Update sender filter for Suffolk County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyGParser.java
M	cadpage-private

[33mcommit 84ce133164eda95c8b1a670375937eabb536029a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 2 07:26:23 2017 -0700

    Ditto

M	cadpage

[33mcommit e32549f42d9c5adcff7f77a16367d445c2f6318a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 2 07:21:56 2017 -0700

    Backed out erroneous change

M	cadpage

[33mcommit a0d7dc3db40bd11a5e07124a8453611fa3aad4ea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue May 2 06:39:33 2017 -0700

    Tried forcing the widgit call count text color to white

M	cadpage

[33mcommit 431f0aa99420b2973767efe4e42ac879ada1207e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 1 21:30:27 2017 -0700

    Update gemone.log

M	cadpage-private

[33mcommit a149dcd3dd8ed10afd19eb17b0b9e0700f2d6575[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 1 21:18:56 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit b37312ad8a1c980dc6603164b77dd2db8365f6fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 1 15:41:22 2017 -0700

    Fixed mapping problem with New Zealand

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgInfo.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLakeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZNZ/ZNZNewZealandParser.java
M	cadpage-private

[33mcommit dda859e742e97a434edb1b99dcaee05fc196db08[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon May 1 14:41:12 2017 -0700

    Fixed parsing problem with El Dorado County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAElDoradoCountyParser.java
M	cadpage-private

[33mcommit 858381b93290ad2043544e8bb503808eccab54dc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 30 20:15:02 2017 -0700

    Update sender filter for Lancaster County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit 57d2099d0ffb786a423c90561717ebc9009c2d43[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 30 19:43:42 2017 -0700

    Fixed parsing problem with Bell County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBellCountyParser.java
M	cadpage-private

[33mcommit 434c938dbbc62ec312ea4e498d49a7be27d9fc6d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 30 12:40:35 2017 -0700

    Update genome.log

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTHartfordParser.java
M	cadpage-private

[33mcommit 23f4726984cf66dd13d9615162ad2acf2d8e4050[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 30 09:48:36 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 72d308254067dbc3c3989b97a0d26fc15337c54f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 30 09:41:21 2017 -0700

    Fixed parsing problem with Williamson County, TN (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyCParser.java
M	cadpage-private

[33mcommit a4035a11510ccd43e0e58ca1e6ae3878acb3c6c4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 29 22:21:15 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ee3139da646b905d25a506563de2c39eb3b1d8ac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 29 16:57:15 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHokeCountyParser.java
M	cadpage-private

[33mcommit cfa9e4f6ff119602e78072b3cc120f38e72f791a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Apr 29 16:19:45 2017 -0700

    general updates.

M	cadpage-private

[33mcommit bfe38b5f73059f2b646e2a98e54e78a588278b7d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 29 09:18:51 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit c59a0608ae5507fe8ae352894d7c8c5e0eae4c7e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 29 09:03:47 2017 -0700

    Fixed parsing problem with Hamilton County, IN (Added D)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyParser.java
M	cadpage-private

[33mcommit f5d90718c066f65ea39effa629981ae33e40d8ea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 28 08:46:08 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 8c7442a584fb5d5ab795126202b36a38747b1599[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 27 17:06:57 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 44b1bec0c8c3f9d934e5e8581be5f724a87341fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 27 16:35:23 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit de1abf8dd74b706894a4081baba610c5103cb521[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 27 12:46:57 2017 -0700

    Update support doc

M	docs/support.txt

[33mcommit 23e1b998dadea56b3bf2b9c6a0107a5fc56c971c[m
Merge: 53d4212 f1a9d98
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 27 12:29:47 2017 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 53d4212af969473b2c84035da5a715aa525ff1d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 27 12:28:53 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COAdamsCountyParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit f1a9d988043ecbcb576dbb40ea4037b48ae03f6e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Apr 27 02:13:56 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 2cfba773335ea5ac3b6d70eb8e4a55260470dbcc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 26 11:14:07 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit d0e5197e736e272964761ef8d842b395e172df39[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 26 10:55:49 2017 -0700

    Update sender filter for Mineral County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMineralCountyBParser.java
M	cadpage-private

[33mcommit 6f29dd5a13431c5f49db9be7ce4ef97f96bfc74f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 26 10:48:01 2017 -0700

    Release 1.9.11-44

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyBParser.java
M	cadpage-private

[33mcommit 53759068a68b6a15acf1444b691a12091e2d2c58[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 26 07:20:49 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 804f6349bfe5f4f336d2e640596ba739886979f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 25 21:30:41 2017 -0700

    Fixed parsing problems with Linn County, OR (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit 47767cc4666ce0107dbb4e580be17c4b14c1c965[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 25 19:21:37 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 4d5c34385539fd4d2223639a7556e2a2e4903f78[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 25 17:39:02 2017 -0700

    Fixed parsing problem with Clarion County, PA (C&E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyEParser.java
M	cadpage-private

[33mcommit baa671fd668e9108aad492b417bd7cb1fa9570f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 25 15:28:49 2017 -0700

    Fixed parsing problem with Madison County, AL

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMadisonCountyParser.java
M	cadpage-private

[33mcommit 9745aeecc40c2c279520525ea8d1788821d2607a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 25 14:49:16 2017 -0700

    Fixed parsing problem with New Castle County, DE (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyBaseParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INElkhartCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchChiefPagingParser.java
M	cadpage-private

[33mcommit f39045cce79dca993845380646810fe427ecc414[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 24 14:35:57 2017 -0700

    Fixed parsing problem with New York City, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNewYorkCityParser.java
M	cadpage-private

[33mcommit 4fdb0c0d9f15f29082fb1c4ed5ea75d48c40fbf3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Apr 24 01:46:32 2017 -0700

    general updates.

M	cadpage-private

[33mcommit fbc5b78635f621ff64217dcc08c50d27af3779f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 23 13:20:45 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 2009172241e5e9fc025069a4f1c9d6b1f128cdf3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 23 13:03:32 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit a8ce4a0e5f1124dc985441f40ebb5fe57ed7ca73[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 23 12:42:40 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 3cc8976bed1ca2eb85a379f22d987f6d0751d064[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 22 09:03:01 2017 -0700

    Release v1.9.11-43

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 499755fa32af9ad9585f4ecb4f60c49399a7d6f1[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Apr 22 02:56:47 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 5166dad8d2e8460c5beb517ebec98c1c98c5c49d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 21 10:39:41 2017 -0700

    Fixed parsing problem with Franklin County, PA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyBParser.java
M	cadpage-private

[33mcommit 76f8a69003e05a24b0959b183658f5f041cf6f0a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 20 16:57:03 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyBParser.java
M	cadpage-private

[33mcommit 782f299ae9ac5a702cebed65f3ff4b692b593ad3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 22:05:39 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMorganCountyParser.java
M	cadpage-private

[33mcommit d49324b27b0f3a953634ac5b040f88f5cf2eda4e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 21:44:32 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit acbf76d9103f0348ee4884b71c6b2e30de1b947b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 19:59:39 2017 -0700

    Fixed minor parsing problem with San Bernardino County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyAParser.java
M	cadpage-private

[33mcommit 620a9e0ba4874ff6ca15ee53b3e2bb9f58a0111a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 19:47:16 2017 -0700

    update call code table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHarrisonCountyBParser.java
M	cadpage-private

[33mcommit c9c9a55765402b83b0f871888b5ea27bd76758a0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 19:41:41 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit c51feb28c23c9b8e7714589cf141147996ce6eec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 19:23:26 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 537643e584e8b55ee046f38129bc0f45088b3dcb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 17:40:02 2017 -0700

    Fixed parsing problem with Monterey County, CA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
M	cadpage-private

[33mcommit 367d00b43f8b6167db5fe514ffacd5ab59c1da4e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 19 14:18:56 2017 -0700

    Fixed parsing problem with Adams County, CO

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COAdamsCountyParser.java
M	cadpage-private

[33mcommit f6637933d4b1a16a55fa62862fce4eefd56430a8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Apr 19 02:29:28 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 078077f7a385e6514c85c21aae510654d353767b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 18:01:19 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 62623b5bef710318b55f7253c4b56b084df42532[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 17:46:40 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 8cdf3822a4da74a6cbde4f0b772269e056aa1d24[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 17:42:07 2017 -0700

    Fixed mapping problem with Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit 1a824b4a9d3aed7b75a68930c4acbad34cbf2e9a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 10:06:33 2017 -0700

    ditto

M	cadpage

[33mcommit 296fd0139be01b6ecc1b24ecde87be4fc5cbbfd9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 09:57:38 2017 -0700

    Release 1.9.11-42

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit bffa1dd991586c8234fdf9ddaf57bb5815806adf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 08:48:13 2017 -0700

    Added GPS Lookkup talbe for Murray County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMurrayCountyParser.java
M	cadpage-private

[33mcommit 16f0d5170fb1cee66405b1dca3404c0e8c1563d4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 08:39:55 2017 -0700

    Fixed parsing problem with Jefferson County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INJeffersonCountyParser.java
M	cadpage-private

[33mcommit ee824060b7a53d2f8c09445b0ed948b5ca6414a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 07:28:11 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyCParser.java
M	cadpage-private

[33mcommit c1f3d9ed578c23ada3edc1608b189e0514eef053[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 18 07:15:52 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit df8a9af2dac6c5620d300e2750d9f5bd418e4dd4[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Apr 18 03:22:22 2017 -0700

    general updates.

M	cadpage-private

[33mcommit ba31c25ecb3e9d1cff4eddf0f8de9d75b35667cf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 17 22:13:47 2017 -0700

    Ditto

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPersonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPersonCountyParser.java
M	cadpage-private

[33mcommit b1229f5c3d45ca20ac2b43c2b8abe5d520ac238a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 17 21:54:58 2017 -0700

    Fixed parsing problem with Person County, NC (A & Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPersonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPersonCountyParser.java
M	cadpage-private

[33mcommit 0f87c7f3e6a9cea5c6fb1538a7745f531b68d33f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 17 15:24:36 2017 -0700

    Fixed parsing problem with Lancaster County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit 625eeabfc20cf5465d743a207b71d81017bf55b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 17 15:06:40 2017 -0700

    Update sender filter for Rutheford COUnty, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRutherfordCountyParser.java
M	cadpage-private

[33mcommit ab8075b9236d2d4cd8aec3a5a21c0b3b81a02079[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 17 14:58:12 2017 -0700

    Fixed parsing problem witih Fresno County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAFresnoCountyParser.java
M	cadpage-private

[33mcommit 0ad372f76638184a400e03bb2d5170101ee8338a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 16 15:09:08 2017 -0700

    Fixed parsing problems with Clackamas/Washington/Multnomah County, OR

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClackamasCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyAParser.java
M	cadpage-private

[33mcommit e6a74deddf9db69946df48d10d140ba3fb4c86b5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 16 10:21:36 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit c39b5759983c922f5dc8aeacb48e91e62f5ba4e2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 15 13:22:28 2017 -0700

    Release v1.9.11-41

M	build.gradle
M	cadpage

[33mcommit dd13dc79649f2a70e993f1fac652b4ccf0c72ec5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 15 13:07:20 2017 -0700

    Fixed msic tests

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAForsythCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAJacksonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMarionCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYDaviessCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAWestFelicianaParishParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIAlconaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIIoscoCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWestchesterCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAErieCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthamptonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCAndersonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMontgomeryCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVLincolnCountyParser.java
M	cadpage-private

[33mcommit 726913d4d0ced4903495ea6ef28d787cf10b540e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 15 08:40:57 2017 -0700

    Fixed parsing problem with Berkeley County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDMontgomeryCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCBerkeleyCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBParser.java
M	cadpage-private

[33mcommit 3980269b50f9ccd81cf781c65e2b8a36c116e18f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Apr 15 02:36:33 2017 -0700

    general updates

M	cadpage-private

[33mcommit bae1146b00a3cccf70e0fa94c8200718d6833c3f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 14 18:19:45 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBullittCountyParser.java
M	cadpage-private

[33mcommit 9cbea544a5cc3d612d881bde7cba8a0081f69827[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 14 16:00:52 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 74a2e285dc3a1c1ca625a517b058a82c6b00a903[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 14 15:33:52 2017 -0700

    Fixed parsin gproblem with O'Fallon, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA33Parser.java
M	cadpage-private

[33mcommit 0e88db2e6d010138eaf3c66b869c6cefb6f11d26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 19:07:53 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit aa7500b504370f3ddc57c2b53c58567765fcc3f0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 18:26:23 2017 -0700

    Update A911 Parser table

M	cadpage

[33mcommit 3d0b84027b4a72d8d7bffd6c965e9cc02e2dd927[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 18:10:41 2017 -0700

    Update A911 Parser table

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyBParser.java
M	cadpage-private

[33mcommit 141b02f05d57354682652ed93fafc141cfefc8a0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 17:59:05 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e0e9288dbd9cc09e53667f9d93fdb92cf4c4cc2f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 15:29:17 2017 -0700

    Release v1.9.11-40

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit b6682499e8ae63e077fff35bc5595186537724e7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 14:23:52 2017 -0700

    Fixed parsing problem with Franklin County, PA (Added B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyParser.java
M	cadpage-private

[33mcommit 7ae7f3ba4d4b17cd10c228264c76c2b8a9d57e12[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 09:59:56 2017 -0700

    Fixed unintential move of notification settings

M	cadpage

[33mcommit 640fc75c811b093c5afe4d4b9f0535fdf781ff4e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 13 09:32:59 2017 -0700

    Fixed parsing problem with Cabell County, WV

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVCabellCountyParser.java
M	cadpage-private

[33mcommit c4f3dfa335b9943d71d55bc69e20aa6db885b1b5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Apr 13 03:09:19 2017 -0700

    general updates.

M	cadpage-private

[33mcommit ed567a2ff0aae42a48a2caf112e5aacd0e69a70f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 14:09:52 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 59a50e1ad5c0cfdf7a0f78e9ded88ee5ed9442ef[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 13:23:00 2017 -0700

    Release v1.9.11-39

M	build.gradle
M	cadpage

[33mcommit 2641305e61b7c848def7f98c525968159957a75a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 10:09:03 2017 -0700

    update genome.log

M	cadpage-private

[33mcommit 92de0ca3e383abfcd826d668c36cd66b7964d10a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 10:08:05 2017 -0700

    Fixed mapping problem with Cherokee County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeCountyParser.java
M	cadpage-private

[33mcommit c03e360efcf29198e1c49136f1a80c7a0af0767d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 09:52:10 2017 -0700

    Fixed parsing problem with King County, WA (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA41Parser.java
M	cadpage-private

[33mcommit 36766ae5ba4dae751ab14d2f12c4c715a4da999e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 08:54:24 2017 -0700

    Fixed parsing problem with Suffolk County, NY (G)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyGParser.java
M	cadpage-private

[33mcommit 7341777014242d3f06cd74f7bacb5adc7ffb07cf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 07:46:24 2017 -0700

    Updage genome.log

M	cadpage-private

[33mcommit 693d4c29ef7b2333b35f92aee5e57f825c8c90de[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 04:16:50 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 14b3d6c7ab2c1d166573108045b377efa57c5769[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 04:09:27 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit c94281eaded463ed3e704e4f620db6a0794cee8d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 03:59:35 2017 -0700

    Minor fix for Cabarrus County, NC (A & B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCabarrusCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyCParser.java
M	cadpage-private

[33mcommit 92c357d757ee38726667236808e6ef15bd69c869[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 12 03:03:47 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyBParser.java
M	cadpage-private

[33mcommit a8fa4ce2d31db7d9da8fe9a7a552552dc5387049[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 11 22:50:11 2017 -0700

    Fixed parsing problem in Suffolk County, NY (G)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyGParser.java
M	cadpage-private

[33mcommit d12363f01fb44adafaafd20a84d5c92b83779585[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 11 20:13:31 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyBParser.java
M	cadpage-private

[33mcommit d2715e6acc3f727e85098bb016d3750559eb10a1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Apr 11 12:33:56 2017 -0700

    New parser UTSaltLakeCountyB

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyParser.java
M	cadpage-private

[33mcommit 821cc61937579aa594ba5c2692bc09a45fede51e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 10 08:26:46 2017 -0700

    Update sender filter for Morris County, NJ

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyAParser.java
M	cadpage-private

[33mcommit a005c667e6d7366350f3b63d926658552911ca56[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 10 07:51:29 2017 -0700

    Rename CAYebaCounty to CAYubaCounty

D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYebaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYubaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 92466f828eb8bd414395a8a563bba0f77f6d7efe[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Apr 9 19:54:11 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 6300554e15a4091ed281c7d56e760b1b4ebd1eba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 9 15:06:56 2017 -0700

    Release v1.9.11-38

M	build.gradle
M	cadpage

[33mcommit 5a0778b4de37f52cf1876f924054600fbc67d688[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 9 12:22:40 2017 -0700

    Added Yeba County, CA

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYebaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 47397716c6a3dc6ed0f400b7f90f4f68b93eb865[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Apr 9 11:35:49 2017 -0700

    Fixed dispatch probelm with Luzerne County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALuzerneCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA41Parser.java
M	cadpage-private

[33mcommit 679c9d67f72ff9e56c3341aee27b2a27dbb339ae[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 8 16:35:04 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 538727604400abd32c23305c21be6f4a1444b3c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 8 15:09:41 2017 -0700

    Fixed parsing probelm with Clarendon County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCClarendonCountyParser.java
M	cadpage-private

[33mcommit 48a6851e31a85211e32d0aeaacc5bc323980be46[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 8 14:57:47 2017 -0700

    Fixed arsing problem with Green County, OH (A&B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyBParser.java
M	cadpage-private

[33mcommit 2872f8836da806595ef6dc65955d825502a6a5e7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 8 11:13:45 2017 -0700

    Fixed mapping problem with Adams County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
M	cadpage-private

[33mcommit e8caa2642f07fc9f8f2b0380cd5f9a472e36cd7e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 8 10:44:43 2017 -0700

    Fixed parsing problem with Davie County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavieCountyParser.java
M	cadpage-private

[33mcommit 3c6950422d4f55d6221f679de2445f3fec006448[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 8 09:16:37 2017 -0700

    Fixed parsing problem with Gordon County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAGordonCountyParser.java
M	cadpage-private

[33mcommit 4c465000f7538e82d70d74021fa0f9f7973a9396[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 8 08:42:59 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 66c38d6c6c0c983842ec3c5fa6976f8954d536bf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 7 22:32:30 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit 182d59884ed9ba856319b9fb03ada1d9b8555981[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Apr 7 22:24:56 2017 -0700

    Fixed parsing problem with Linn County, OR (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-private

[33mcommit 745c3e406121d6f6985712dbc7e82f17228ae85f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Apr 7 00:21:11 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 0924286907e3c1ff8706cc5d97d301231c07ccb3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 6 19:05:51 2017 -0700

    Release v1.9.11-37

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit 006c64f54fdfd0313e42c30d34fcbd82d467a960[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Apr 6 09:07:53 2017 -0700

    Fixed parsing problem with Burlington County, NJ (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyCParser.java
M	cadpage-private

[33mcommit 2f7bc2f468d1214ffe6ead56634a8403a4919424[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 5 22:22:14 2017 -0700

    Fixed display problem with Waukesha County, WI (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyAParser.java
M	cadpage-private

[33mcommit fe07cad04df069d1e6153e1315f6c41c82d26949[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 5 21:54:09 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 95cd2f5baa846cc777d5c08b1026c291a8b76984[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 5 20:56:50 2017 -0700

    Fixed parsing problem with Lancaster COunty, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit f220ab7091a5d509943ba6bb466370676820851f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Apr 5 09:06:03 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit ee9b53863e0348f30542a5adeec2fe60403187ed[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Apr 5 01:54:04 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 92081a7c8c68aec4d6b69141ad8d1665d0f27135[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 23:29:40 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 0505fdfc1dd6bc067164494b06dc56cc4d20e176[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 23:21:51 2017 -0700

    Release 1.9.11-36

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit d4cc50df2306dbf0ec9f9f7f6d9844b56ffb3cd1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 21:46:28 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit 23ec928646da3abb8193d5e3a428c92347380f8b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 21:24:22 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit 4c9392ad4f9059442c7bf0847d884abb08dd8dd9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 21:11:38 2017 -0700

    Checking in Garland County, AR (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA46Parser.java
M	cadpage-private

[33mcommit 9bc50fc38b274a1294bf0ade84988dbee09ea722[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 21:00:00 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit f82b07f8a1435cf3f6ff9fd1a951aed11c813760[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 20:54:10 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit 2cb88de88a31a79bbeb46610deabd35d1148f546[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 20:24:00 2017 -0700

    Checking in WHite COunty, TN

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Message.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MessageBuilder.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SplitMsgOptions.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SplitMsgOptionsCustom.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWhiteCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
M	cadpage-private

[33mcommit 4fca59cb8a131ef18a4992292487cc8309e75de1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 17:40:51 2017 -0700

    Fixed test class problems

M	cadpage-private

[33mcommit 1b35e1aad61a40961971961dacba92e48aa5191f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 17:04:35 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 02f844c6002a1d530f821b3a7ac21c9482de9c72[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 16:55:56 2017 -0700

    Fixed parsing problem with Somerset County, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSomersetCountyAParser.java
M	cadpage-private

[33mcommit b9e8c5d1c1827f68ae9cf0f18601fc88aa9f15eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 16:34:47 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 82809e5392b6295a324fd97cea1363f722d8e4f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 16:16:42 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit cb4c19111ce5263dee5e81244c4affef92f760b1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 15:12:48 2017 -0700

    Fixed parsing problem with Montgomery County, MD (added C)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDMontgomeryCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDMontgomeryCountyParser.java
M	cadpage-private

[33mcommit 50fe16a53883dd63248243ec84d1bfdd9b44ca20[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 12:38:34 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 98cc5c3bea09426e9d7472bd4b571746709c3e07[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Apr 3 12:33:30 2017 -0700

    Fixed parsing problem with Talladega County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALTalladegaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-private

[33mcommit 78364854bbf2c01d93fa0e2c50f1e2635bcf7670[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Apr 3 01:06:26 2017 -0700

    general updates.

M	cadpage-private

[33mcommit f5afc773520d6f5818dc7c7a0758a0c103f1cbfa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 1 13:56:30 2017 -0700

    Fixed parsing problem with Greene County, OH (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyBParser.java
M	cadpage-private

[33mcommit 78f4e68b693a66e200bf30bf6b760176b5915905[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 1 13:00:13 2017 -0700

    Fixed parsing problem with Red Deer County, AB

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA51Parser.java
M	cadpage-private

[33mcommit 322f5e6000da06a1cb25666697cf9d14bbe47e25[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 1 12:05:34 2017 -0700

    Fixed parsing problem with St Louis County,  MO (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyEParser.java
M	cadpage-private

[33mcommit 87579535f531e83ac9c35a766480717dfefe3562[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Apr 1 11:58:05 2017 -0700

    Fixed parsing problem with Charleston County, SC (added C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyParser.java
M	cadpage-private

[33mcommit f9bf6f0ace1f35c0a39746ba19fdd3deb2d70c77[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Apr 1 01:11:42 2017 -0700

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit d79eb6466bd735ba068c4340ed5f931538a805c2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 31 19:45:35 2017 -0700

    Fixed parsing problem with Lincoln County, SD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDLincolnCountyParser.java
M	cadpage-private

[33mcommit f0796d3328039c74de3bb0793134b2ae0746712f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Mar 31 16:03:31 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 3685c87796d25f6867e24449622150e5cd2513b8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Mar 30 19:06:05 2017 -0700

    general updates.

M	cadpage-private

[33mcommit b7b689f9ae5db73b2b6a2e7847d92e87efd4717f[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Mar 30 16:06:49 2017 -0700

    parsers mostly, but a little of everything;

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARGarlandCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGarrardCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCumberlandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWhiteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHidalgoCountyParser.java
M	cadpage-private

[33mcommit f64c74ca575ee93252961a57bd0b86417795b442[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 29 13:38:13 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 38355ce7df324425c57f57956a5e8dee5ddb72e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 29 13:14:30 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 00cee517c6a76b4baa337605345d344b13928cf7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 29 12:19:02 2017 -0700

    Fixed parsing problem with Bucks County, PA (A&B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyBaseParser.java
M	cadpage-private

[33mcommit 1d3e42075afb41ffef15e3c4abb313efdeaaa074[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 29 09:21:19 2017 -0700

    Release 1.9.11-35

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 2db5f1214835f2ce6e22702f90c88f90be504bc1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 29 08:03:33 2017 -0700

    Fixed parsing problem with Bucks County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyAParser.java
M	cadpage-private

[33mcommit 5bdf57b563b894bc270df2aa52980196ef28f781[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 28 19:08:42 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLWashingtonCountyParser.java
M	cadpage-private

[33mcommit 04485941a9ae4507f5a1d180b194c166a42313da[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 28 18:44:33 2017 -0700

    Fixed mapping problem with Campbell County, KY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCampbellCountyParser.java
M	cadpage-private

[33mcommit 849443d66f956f3cd429541864fc40247717cd1a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 28 18:08:44 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
M	cadpage-private

[33mcommit 42f416c3fe60d3028c5b84735b1470c577172f07[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 28 17:48:42 2017 -0700

    Fixed parsing problem with St Lawrence County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYStLawrenceCountyParser.java
M	cadpage-private

[33mcommit e2973682c02502dfde48971ffb41d38aa630b5a1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 27 13:33:01 2017 -0700

    Release v1.9.11-34

M	.gitignore
M	build.gradle
M	cadpage
M	gradle/wrapper/gradle-wrapper.properties

[33mcommit 3d41e5858e9ac11f497a8499a75e0451b554bfe2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 26 08:38:08 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 7b7c9e17dad988f93e0dda7a75dc2706cb19e0a8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 25 21:19:44 2017 -0700

    Fixed parsing problem with Tolland County, CT (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
M	cadpage-private

[33mcommit d9b2b2a08c71a21d943d9e1110862bc441b40781[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 25 18:45:58 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 87f9b46170735b56a3b0ea0c6458ce2484521dc2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 25 17:40:14 2017 -0700

    Fixed parsing probelm with Greene County, OH (Add B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyParser.java
M	cadpage-private

[33mcommit 3f0a695995ba641231fc823c42c85669dbb4117c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 25 12:08:58 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 8f37b3bc398d19ac1b225d01db29f0abefef4311[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 25 10:54:29 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit d81ff40fa438fe21bf81f79478ffe7efa266857e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 24 20:32:25 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 6fa8d84cd76cd7d6aa85698ff6f06d221f86969e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 24 20:25:28 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 93a5d18800f239053e543d42ac81a55c7c742e22[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 24 19:38:15 2017 -0700

    Fixed parsing problem with Greene County, MO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOGreeneCountyBParser.java
M	cadpage-private

[33mcommit d3b0f5610758a306e37215d3b5b5acdb60cc5c4b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Mar 24 00:48:28 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 62bef1679f61903adf6d048a3c7b40847954e47d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 23 20:37:11 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit ad830aff5bad213bddba12a4b706a9998970e149[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 23 14:56:50 2017 -0700

    Update A911 parser table

M	cadpage

[33mcommit b242e3bd4d2effe876e8abddb5ce44db66a3daba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 23 14:47:29 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 31841ae7edae527cdc1414f5d34e718db42de715[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 23 13:14:37 2017 -0700

    Checking in Bibb County, AL

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBibbCountyParser.java
M	cadpage-private

[33mcommit 6bc5dcf15670e3d3ccc51ccc400373af95d80ba6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 22 21:17:52 2017 -0700

    Update genome.log

M	cadpage-private

[33mcommit aa1777b76f512fa98fe492fed85c0babf81a9481[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 22 21:12:05 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 75c6de99f1f900e8e66f2a66759da3b7f6a2bf46[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 22 20:14:33 2017 -0700

    Checking in Adams County, CO (B)

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 5389a206d43648dc712b8af620b5f51187d174a9[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Wed Mar 22 19:04:44 2017 -0700

    skeletons

M	cadpage-private

[33mcommit b0ca7b154ddac4a47906b9e37defa4af39405662[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 22 11:50:20 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 5ae80fa04c3a13de54599cc21c1a7911216026c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 22 11:05:59 2017 -0700

    Fixed parsing problem with Delaware County, PA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyBParser.java
M	cadpage-private

[33mcommit e7d25659b95deb525419d966727f00a77feea1e7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 22 09:55:37 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 113e9cb98fad7585f3aa9b3e92c8f588a8a1913b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 22 09:34:34 2017 -0700

    Fixed parsing problem with Greene County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyParser.java
M	cadpage-private

[33mcommit b9bcb2df54c86c219f4a4241b11adb16cdfbe0e2[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 21 23:36:40 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 47a615e51d4887a0ee6002c6458378d2ce18ec35[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 22:04:21 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit e90997b4a6f165388b35f24db8bdb01709d899ae[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 19:02:37 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 5981bd544c48f5cd1dffdca368605d2aacd8cba5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 18:23:52 2017 -0700

    Fixed parsing problem with Litchfield County, CT (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyAParser.java
M	cadpage-private

[33mcommit 85b4fd6c3f6118a1b253e36bc6e588c7debad9cb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 16:31:09 2017 -0700

    Update call table for Richie County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHarrisonCountyBParser.java
M	cadpage-private

[33mcommit 5392230a3a2cf6e08cef109bb9ff1433eb07182b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 16:23:46 2017 -0700

    Fixed parsing problem with Wright County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOWrightCountyParser.java
M	cadpage-private

[33mcommit cbb92d02be674b3a36e3a1a106d961d2b140f7e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 16:07:16 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 73bf02817b962beb276a62ab34f34e555039ca3b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 15:49:06 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1ff2cd5d3e9a9e8dba7d3ed50ecd20925050a9b3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 15:00:41 2017 -0700

    Fixed parsing problem with Montcalm County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMontcalmCountyParser.java
M	cadpage-private

[33mcommit 144b614d489f52aa8fff4b8c312e388f07091d9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 21 09:20:22 2017 -0700

    Fixed parsing problem with Franklin County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCFranklinCountyParser.java
M	cadpage-private

[33mcommit 56b7966342cffa066482356e1f9333fa58434e9f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 21 01:44:30 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 28834a9f4eeab648549569cf2c87801071dfca26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 20 19:22:09 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 11ddf0327438981c41eaffafc74307a4f7665108[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 20 18:57:53 2017 -0700

    Fixed call code table entry for Greene County, MO (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOGreeneCountyBParser.java
M	cadpage-private

[33mcommit d8ad1b3f000c66cad621741aa62ccb9acd0ccd2a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 20 18:51:06 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 1adb9629028a52b9dc010a0f209dfafe71eedf26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 20 18:30:21 2017 -0700

    Fixed parsing problem with Minnneapollis/St Paul, MN (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchPrintrakParser.java
M	cadpage-private

[33mcommit 0f3f50d288201148b6bc984baa729aed2d7ecf50[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 20 17:30:25 2017 -0700

    Checking in Caledonia County, VT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTCaledoniaCountyParser.java
M	cadpage-private

[33mcommit 6519acf997ab70e95b49ae3c9efdaca2db353719[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 20 16:41:21 2017 -0700

    Fixed parser test problems

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAAvoyellesParishParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVRoaneCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA46Parser.java
M	cadpage-private

[33mcommit e8acb72b5714a5816e369bf136f71b668b12724e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Mar 20 14:16:28 2017 -0700

    general updates.

M	cadpage-private

[33mcommit 717b95b63f3436c995b8f294726afec48396cd2b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 20 12:04:27 2017 -0700

    Fixed parsing problem with Pulaski County, AR (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADinwiddieCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA46Parser.java
M	cadpage-private

[33mcommit 8fce44534fa4bf219240eaae06368a2526d6fd80[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 19 13:52:13 2017 -0700

    release v1.9.11-32

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBooneCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYFultonCountyParser.java
M	cadpage-private

[33mcommit 09d37db6778a580d08031dd62d080cf57a0752f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 18 19:01:00 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 92f4172623e71e2650acccdd137b22e82bd6a624[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 18 18:22:53 2017 -0700

    update msg doc

M	cadpage-private

[33mcommit 939665c15de522288633127016f9c8f80fd108c1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 18 18:08:01 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MONodawayCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBCParser.java
M	cadpage-private

[33mcommit 5a8448853fe054ba0b4ea252985cbd8ed1446753[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 18 17:17:41 2017 -0700

    Update sender filter fro Acadian Ambulance, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceParser.java
M	cadpage-private

[33mcommit de85e8aa95ccf1c63e2d525bbcebff321af57c8c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 18 16:58:48 2017 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit b541fddbe9f9a2fef90d276f4871759942b9abb0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 18 16:27:26 2017 -0700

    Fixed parsing problem with New Haven County, CT (B)
    Update sender filter for Rhea County, TN

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNRheaCountyParser.java
M	cadpage-private

[33mcommit bf49b67a65024ab52cab8f6e3aed0c4641d60c0d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Mar 17 02:27:26 2017 -0700

    general updates.

M	cadpage-private

[33mcommit bad3a5eaf1f58866cee0b0cbfd88a1555c01e05c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 15 20:52:47 2017 -0700

    Checking in Tarrant County, TX (D)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMidlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTarrantCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTarrantCountyParser.java
M	cadpage-private

[33mcommit 375c77fcb7ba278cd463efe2197cfa75f5319ba1[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Mar 15 02:33:22 2017 -0700

    general updates.

M	cadpage-private

[33mcommit fa791abe79cdfbffe25114d205669aecd2ff649e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 13 17:24:41 2017 -0700

    Fixed parsing problem with Boone County, KY (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBooneCountyAParser.java
M	cadpage-private

[33mcommit c4d15ee5e9dc4cd1c1f5a51ecefb225136eb785c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 13 16:42:13 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit ee7c6bf92661245a4fac6c0ec67a297f3e0f27b0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 13 16:35:25 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit cbf175b4b5c4ed9921026feeb375488b60c52b33[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 13 16:27:13 2017 -0700

    Fixed parsing problem with Midland County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMidlandCountyParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit 243e11f8a006856b41f8e0ade1cda588e237cae3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 13 08:51:44 2017 -0700

    Fixed parsing problem with Fulton County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYFultonCountyParser.java
M	cadpage-private

[33mcommit 5c3f84d88c92992df35c8757c98730b668a0872c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Mar 13 01:18:39 2017 -0700

    general updates.

M	cadpage-private

[33mcommit eda6a17fa961bbac9e23c2c09ad648eb23f51ecc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 12 23:41:08 2017 -0700

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMontcalmCountyParser.java
M	cadpage-private

[33mcommit 3f3c43b739f951beeeae8a087051f0539aa0089b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 12 18:02:03 2017 -0700

    Update sender filter for Stark County, OH (Redcenter2)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyRedcenter2Parser.java
M	cadpage-private

[33mcommit 583aa0a3df8bde8a7ece136c68cd531ebca32f18[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 12 17:49:31 2017 -0700

    Fixed parsing problem with Hamilton County, IN (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyCParser.java
M	cadpage-private

[33mcommit 7d6bdaf7e5336d84cc82c302b06d397d3aca8d37[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 12 17:31:17 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 56ee113fe47704bf1d9bccd92b3ab33ec2a5ab0f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 12 17:06:50 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit b38714806c11d09ae98f494ebe658ac045f3d65e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 12 16:42:37 2017 -0700

    Update msg doc

M	cadpage-private

[33mcommit 15ce12fd229848eb733a406c072f5e5cdca19e3d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 12 16:08:52 2017 -0700

    Fixed parsing problem with DuPage County, IL (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyAParser.java
M	cadpage-private

[33mcommit 4f3fb452a1ed259e091d49adfad33c39538603bd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 11 01:15:00 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 68faa219225a20cc15c3303a40e563a72e432ad2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 9 21:26:23 2017 -0800

    release v1.9.11-31

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit a5ab2e72c8995b5d2851c75af788f035505efa27[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Mar 9 15:33:10 2017 -0800

    ADDR+SKELS

M	cadpage-private

[33mcommit 2a87ff756bbc574be6f8de7eb7214c09b4673ad5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Mar 9 14:32:30 2017 -0800

    general updates

M	cadpage-private

[33mcommit c43ca6783d558bdaa164fd1cc75d61bb02a842c4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 9 09:36:14 2017 -0800

    Fixed test failures

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyBParser.java
M	cadpage-private

[33mcommit cb1cfee412d632c999dab90267ca88b70f364386[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 9 08:56:25 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit ce9d8de21ea341e39e765a67d83d09122db57dc4[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Mar 9 00:47:35 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 34deb3cc34078e59381f5ac44d2690789a4d758c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 8 19:41:17 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLawrenceCountyParser.java
M	cadpage-private

[33mcommit 7bd01bf4aaff8507f1f140c6ff4c5710cdd07768[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 8 18:59:08 2017 -0800

    Fixed parsing problem with Jefferson County, AL (I)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyIParser.java
M	cadpage-private

[33mcommit c0b85d198b175b557c47a66b8a3f05af8fadde13[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 8 18:04:23 2017 -0800

    Fixed parsing problem with Cook County, IL (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyDParser.java
M	cadpage-private

[33mcommit ca923342dcecc0ad156e77ea8f124da407147ef0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 8 15:26:44 2017 -0800

    Fixed parsing problem with Hamilton County, IN (B & added C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyParser.java
M	cadpage-private

[33mcommit 99b439fe95cedc7a456111cbc5b1bb0c850981cc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 8 11:03:44 2017 -0800

    update Dinwiddle County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADinwiddieCountyParser.java
M	cadpage-private

[33mcommit e07d9a21d99b436a503685bdfab8bb3d5debc6a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 8 10:43:38 2017 -0800

    Update DeSoto County, MS (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyBParser.java
M	cadpage-private

[33mcommit 20ec046f0431c3ace6b8eeca69e21c41fc10c17f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 8 09:04:09 2017 -0800

    Fixed parsing problem with Jeffferson County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHJeffersonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit f688aa0ec872fa1e81f1eb861ffdfb1f5cfcd744[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Mar 8 00:49:18 2017 -0800

    general updates.

M	cadpage-private

[33mcommit b913ddf7f4d314a6e8ecefd0fddac38b00c0fa91[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 7 22:03:15 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit acd0d35064a8c01884eff9671f78922b7fbc2e5c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 7 21:49:03 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNScottCountyParser.java
M	cadpage-private

[33mcommit 25688c2ba227adc2c7fac77adb61425bf327dc9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 7 18:09:47 2017 -0800

    Update GPS lookup for Sacramento County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASacramentoCountyParser.java
M	cadpage-private

[33mcommit 08e0d824cab6ac13b3c95dd3563f56ece417b27e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 7 17:49:17 2017 -0800

    Fixed parsing problem with Midland County,  MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMidlandCountyParser.java
M	cadpage-private

[33mcommit 48165ee30a970ed6c37e1315595815d64c1cabe1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 7 13:53:32 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 327f0ecc0488b35717dc60d73664a89bdce5572d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Mar 7 13:45:10 2017 -0800

    Fixed parsing problem with Grays Harbor County, WA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAGraysHarborCountyAParser.java
M	cadpage-private

[33mcommit 5c80482a306166e8d37f63354f5351418932a806[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 7 02:20:56 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 53c90b0c47b79584298a6135e2dcbb54548f933b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 7 02:12:58 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 53a1a5e7339a45f5813801da8446abb5ce08cec6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Mar 7 01:59:01 2017 -0800

    general updates.

M	cadpage-private

[33mcommit d5297f739384b00fc692b30696f443eacbd92b6b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 6 21:05:16 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 83f2f31d4ba55643d7a49e539ceaa8a756799d11[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 6 20:17:49 2017 -0800

    Added split msg options to Nueces County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNuecesCountyParser.java
M	cadpage-private

[33mcommit 8c2cce12f41f43f4b730489c4ff17dd87bed389b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 6 19:57:49 2017 -0800

    Fixed GPS table for Clatsop County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClatsopCountyParser.java
M	cadpage-private

[33mcommit b787a747df0e97c40c4112d73e905d739667cfd8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 6 19:36:33 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 5426410f304ae466a00604e433bb8b432a27d98e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 6 15:37:24 2017 -0800

    Checking in Carbon County, UT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA55Parser.java
M	cadpage-private

[33mcommit 8a23b541a90eda6ea290f29ff7a3c60cff7b74d8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Mar 6 15:30:22 2017 -0800

    Fixed test issues

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHRichlandCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA68Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH02Parser.java
M	cadpage-private
D	hs_err_pid22484.log

[33mcommit bbbf90ccc108f75cdaf8b67038fbf82ea9123f61[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Sun Mar 5 15:53:46 2017 -0800

    ONE ADDR CHECK

M	cadpage-private

[33mcommit e99f2c431be31e485a2a7edfeda168b43966c8c2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 5 09:36:37 2017 -0800

    Fixed parsing problem with Cuyahoga County, OH (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-private

[33mcommit ef6662615cc2dbbd0c6684b6a893d52f55a591a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Mar 5 09:09:24 2017 -0800

    Fixed parsing problem with Kankakee County, IL

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKankakeeCountyParser.java
M	cadpage-private

[33mcommit 06a2280f99bf544fb5c0a24a4f86cd5634e17bc6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Mar 4 19:03:36 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 5573c0bacd28b3756b1a479625c9ed08c0de008d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 4 08:19:05 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 8a9cd0f0e3e9b394428f204026270ea64442886b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 4 00:26:32 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 7e6ef6c728ad6b3889a27bbafa6e9335e126c62b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Mar 4 00:10:52 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 9affed5a2766043eca001213d5bf0901fcb3ad38[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Mar 3 09:19:40 2017 -0800

    Fixed parsing problem with Pitt County, NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchOSSIParser.java
M	cadpage-private

[33mcommit cc0e872ed329dc2e01ed7ad75d103ec32f2cc550[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 2 20:23:47 2017 -0800

    Update sender filter for Wayne County, OH (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWayneCountyCParser.java
M	cadpage-private

[33mcommit 08fc1f16c44afbc0bea10ddbd820905631cd3817[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 2 20:15:45 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit db43624dfcce2d05ab45b2a3ee70a96b67e3bce3[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Mar 2 15:19:39 2017 -0800

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYTaylorCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHRichlandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTCarbonCountyParser.java
M	cadpage-private

[33mcommit 8c99ade2c657faa2e375d425bdac91e27e25f766[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 2 10:58:59 2017 -0800

    Checking in Dallas County, TX (D)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA53Parser.java
M	cadpage-private

[33mcommit 4ca4993c0f96453e9ae70ba5c116576bde057f42[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 2 10:40:50 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f447a372574f2412acb2a07b76f09afe97a7b98e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 2 10:26:07 2017 -0800

    Checking in Henderson County, TX

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHendersonCountyParser.java
M	cadpage-private

[33mcommit 7859afa11d8df0e0bece1cef15e4bfb63ea80fe5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 2 09:28:56 2017 -0800

    Added Richland County, OH (B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHRichlandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHRichlandCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit f6016219c966dc52ce293786df7e8def62cde7fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Mar 2 07:45:34 2017 -0800

    Checking in DeKalb County, IN

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INDeKalbCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA68Parser.java
M	cadpage-private

[33mcommit d51f83722d8118e9bb694f7b6ff3e7bd7614f6a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 1 18:24:19 2017 -0800

    Update misc stuff

M	cadpage
M	cadpage-private

[33mcommit 1b0dc51f6469b22c1d5f3f502e9b016231d27408[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 1 15:01:34 2017 -0800

    Release v1.9.11-30

M	build.gradle
M	cadpage

[33mcommit 0a0228eb33ad52b139465a2bdcace832d6d36fca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 1 14:32:42 2017 -0800

    Update A911 parser table

M	cadpage

[33mcommit 0ef84596e0fa4dc487ab43febf46053f870d7c08[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 1 14:29:11 2017 -0800

    Fixed parsing problem with Frederick County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDFrederickCountyParser.java
M	cadpage-private

[33mcommit b652954310e205dfd015ff0e5b1e28e411a139ed[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 1 11:32:06 2017 -0800

    New Location: Allegheny County, PA (C)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyParser.java
M	cadpage-private

[33mcommit 1c607e9fe1c98e86ef5ba763bde3b2ec148bd031[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Mar 1 08:03:00 2017 -0800

    Fixed parsing problem with Chaves County, NM

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NM/NMChavesCountyParser.java
M	cadpage-private

[33mcommit dfe7dccfb17819e61352f811f2b3a5eeead33d48[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Mar 1 01:22:29 2017 -0800

    general updates

M	cadpage-private

[33mcommit c1771cf8e7e0da8e03c53c51d1cf6c2d5d3e9a4b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 28 10:19:11 2017 -0800

    Updage genome.log

M	cadpage-private

[33mcommit b92f357824cec52b6af3240996bab07102d21ec6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 28 08:38:17 2017 -0800

    Fixed parsing problem witih Oneida County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOneidaCountyParser.java
M	cadpage-private

[33mcommit 029099339a463dfdee44e1476d31ca1f8038a18b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Feb 28 03:06:26 2017 -0800

    general updates.

M	cadpage-private

[33mcommit f1c0303a801e7d2d8027ad81de22144e21c00047[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 27 12:29:15 2017 -0800

    Release v1.9.11-29

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWashingtonCountyAParser.java
M	cadpage-private

[33mcommit 54bfbb25fd5f74fc3ba85bdeb43f530e0fac5267[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 27 10:36:51 2017 -0800

    Back up previous C2DM processing change

M	cadpage

[33mcommit a394193a6d627a424c693da9fa424039707b9bd9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 27 09:09:40 2017 -0800

    Fixed parsing problem with Washtenaw County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIWashtenawCountyParser.java
M	cadpage-private

[33mcommit 06566bb10fb8a1b0be233f1aaf24539a36742795[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 27 07:20:06 2017 -0800

    Checking in Tallapoosa County, FL

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALTallapoosaCountyParser.java
M	cadpage-private

[33mcommit ab4e2e00bda30a4184a42e889fa568e9bb2618b2[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Feb 27 03:08:08 2017 -0800

    general updates.

M	cadpage-private

[33mcommit c345dab138549859211127834db6a8f32536e8f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 26 19:16:55 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-private
A	hs_err_pid22484.log

[33mcommit 98061b9b38281b0078a370948258e50d8c91c62b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 26 18:26:13 2017 -0800

    Fixed problem with CodeMessaging sending page format in C2DM_Loc parameter

M	cadpage

[33mcommit 923ae113b770c6d2de1df1b42b3f92f5cbc43058[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 26 12:52:56 2017 -0800

    Fixed parsing problem with McKean County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMcKeanCountyParser.java
M	cadpage-private

[33mcommit ef39c58aeee16c20e3e92848fb006cd7c53c4341[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 26 12:42:46 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 3d1523d3c8c3809d3a8798040d733a052eed5d94[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 26 12:39:33 2017 -0800

    Fixed parsing problem with Washington County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWashingtonCountyAParser.java
M	cadpage-private

[33mcommit 36f12c3930e7d18daa2ef4b32d2e0c3e3d2c83e4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 26 12:21:01 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 80f002b71311ccb2b0f0792887891b4b28ae1448[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 25 11:54:40 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClatsopCountyParser.java
M	cadpage-private

[33mcommit 9f9f7f95197cea821fb8eda6547eb4889d423c72[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 25 08:32:15 2017 -0800

    Release 1.9.11-28

M	build.gradle
M	cadpage

[33mcommit cb646151c011d58fec732d2fa96ce67b83f29944[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 25 05:49:30 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYJohnsonCountyParser.java
M	cadpage-private

[33mcommit c9241cf1ba718af4e0f2d5e75f464fcda8907a03[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 25 05:28:51 2017 -0800

    Fixed parsing problem with Pocahontas County, WV

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMcKeanCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVPocahontasCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit eddf12043ae5594c9d3df86c3474207e0d5df6aa[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Feb 25 00:28:13 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 504d8cf9f004eb7f03e6a8eb1dee2b58fc20cdb1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 24 22:20:26 2017 -0800

    Added new location: McKean County, PA

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMcKeanCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit eb5219f5ba2dedd0e9bd9fab233d20c8cd3cc579[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 24 20:58:54 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 7814ee922f4e9ff0a64c1aaf660512cca7af5aed[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 24 08:38:18 2017 -0800

    Fixed parsing problem with Johnson County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJohnsonCountyParser.java
M	cadpage-private

[33mcommit 6fb8a5299dc8af31df54cabf68d7c7dde774c58c[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Feb 23 15:48:22 2017 -0800

    skeletons

M	cadpage-private

[33mcommit 2d84fc0f2c106ab0d3e4d914d717c659f579838e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 12:55:46 2017 -0800

    Release v1.9.11-27

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GroupBaseParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GroupBestParser.java
M	cadpage-private

[33mcommit 972049f3e821e3f96d6c7c37c024b3462127f01c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 11:11:42 2017 -0800

    Fixed parsing problem with New Londong County, CT (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA16Parser.java
M	cadpage-private

[33mcommit 1501891b9bdac41614bc69997e7057010e44dd2f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 11:05:09 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f29f2ec6890ba1106c8f6f334c2ad0cb6339d626[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 10:57:36 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 25695df36343522677f85d327631deabeafc048d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 10:47:49 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f9f2ccf09df6a593ff0409140dd1d25d1fa48ef4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 10:39:07 2017 -0800

    Fixed parsing problem with Jefferson County, AL (C, added I)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyIParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyParser.java
M	cadpage-private

[33mcommit 6ff65cc99abf88cca70e5df9244b5661abfa1f52[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 09:08:05 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 29f0db80510e7d37e5cbb01e8357509fbe0b2030[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 23 09:01:54 2017 -0800

    Fixed parsing problem with Duplin County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDuplinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 4f57636d9a8dddde86bfe7de41d34dbcf036f77b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 22 22:26:20 2017 -0800

    Fixed parsing problem with Woodbury County, IA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWoodburyCountyBParser.java
M	cadpage-private

[33mcommit ff0e18426f42fdc042029b1125f18254509e5dfe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 22 17:36:24 2017 -0800

    Fixed parsing problem with Pulaski County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPulaskiCountyParser.java
M	cadpage-private

[33mcommit bbdf2c8afb0694747e1dca9fd114cbf5a8fcfa11[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 22 17:06:53 2017 -0800

    Fixed parsing problem with Manatee County, FL

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLManateeCountyParser.java
M	cadpage-private

[33mcommit 61bf525c58668a133b4dbf6db6cd73dd801e83eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 22 15:36:22 2017 -0800

    Update A911 parser table

M	cadpage-private

[33mcommit f20e991b0472c7f0d5fc540e68308435033bba55[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Feb 22 02:23:27 2017 -0800

    general updates.

M	cadpage-private

[33mcommit c0a1ca7ae24770b5f854560f6a11546f8807ad88[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 21 21:31:59 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-private

[33mcommit 2af080a84cdaf65a9b98624dec7fe124315f9ff2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 21 21:13:18 2017 -0800

    Checking in Salem County, NJ (C)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSalemCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSalemCountyParser.java
M	cadpage-private

[33mcommit 3881eff4dd5be74bacadda37fd04e50fec5e39c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 21 17:44:30 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 8e2f330a6834fb84be22d901516ce396d6c626db[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 20 08:32:24 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 581af9ea04bdd61a0c3875b021add3bd49205502[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 20 08:10:27 2017 -0800

    Fixed release date

M	cadpage

[33mcommit 81b7bb315250e9d1747fba422841e4fe2656c049[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Feb 19 23:47:59 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit a34ad6897931bd891ce988d1eecb29c6aab8a47b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 19 17:16:15 2017 -0800

    Release 1.9.11-26

M	build.gradle
M	cadpage

[33mcommit e4ab6920e5f2955788abb0237967169e7a367bdc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 19 15:32:42 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 736f6e05e5b5ef54ca688198f98ca743e1012e69[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 19 10:59:27 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 2c1a899bd14ed922372c1a4221222fa3bb2a6996[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 18 22:39:47 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 363abdc98a40a8439a1ff794f322d8e9fd0ab32e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 18 22:35:35 2017 -0800

    Fixed parsing problem with Grays Harbor County, WA (added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAGraysHarborCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAGraysHarborCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAGraysHarborCountyParser.java
M	cadpage-private

[33mcommit e476f9ce2d34e0823b8a9eeff6e7b437ea104d93[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 18 21:38:15 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 496ce81d75310792fde7ecc2eea45bd27ee5fa15[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 18 21:22:25 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 9cd6117aec948b76025ce08273a9c05f050db62a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 18 21:06:51 2017 -0800

    Fixed parsing problem with Johnson County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJohnsonCountyParser.java
M	cadpage-private

[33mcommit c5e32c952938d37ba7fe32112b6466d212a3740b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 18 20:51:46 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyBParser.java
M	cadpage-private

[33mcommit bd72b06b63e5d08890ea4db3dad17d325c308475[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 17 09:03:53 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit e240ac8328c3d1ac1e8d359f050187806ebe3aad[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 17 08:59:37 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyAParser.java
M	cadpage-private

[33mcommit e2aac2d2475c36d6df071836295be9d89b4bda3a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Feb 17 01:33:07 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 90abc97145632d25a780a2f593b82fa291f2f13d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 16 12:13:13 2017 -0800

    Fixed parsing problem with Waukesha County, WI (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyAParser.java
M	cadpage-private

[33mcommit 1dd22fec1c6321b67dcbeb03c716bf0078051a42[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 16 11:15:10 2017 -0800

    Fixed parsing problem with Lewis County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WALewisCountyParser.java
M	cadpage-private

[33mcommit 2b6e10a7e0c47aa69694f11818718b760f85aa5f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 20:37:49 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 5992b2c4445de84e11a0e0d09dc6ed4fc7efd484[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 20:29:35 2017 -0800

    Fixed parsing problem with Lackawanna County, PA (A) & Marion County, OR (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyAParser.java
M	cadpage-private

[33mcommit b9d4b480b2ac3d61d56bd6a827602cbcb42b9a06[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 19:01:12 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f4bb377fad5e96db2803f3e64f2a95684926b0ec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 10:09:34 2017 -0800

    Release v1.9.11-25

M	build.gradle
M	cadpage

[33mcommit 25796fb24186d287d5e9b6c3d2248b8feac2fce5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 09:25:26 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit d764621457239e7705caaf793c197d71bd9a5820[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 08:55:53 2017 -0800

    Fixed parsing problem with Cuyahoga County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
M	cadpage-private

[33mcommit b7be780af7a0e823d7d16351eb2441728651b7a3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 08:40:45 2017 -0800

    Fixed mapping problem with Clatsop County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClatsopCountyParser.java
M	cadpage-private

[33mcommit 158c04952299545a5a59d8f6d69140a2a874c272[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 08:28:46 2017 -0800

    Ditto

M	cadpage-private

[33mcommit 3f44717e1f1e93fd45f24dd66cd7b18083c9e3c9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 15 08:28:14 2017 -0800

    Fixed parsing problem with Dade County, GA (added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADadeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADadeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADadeCountyParser.java
M	cadpage-private

[33mcommit 325a458e5a4b6a1e2663f3c2f26a7f8ba64aeaa5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Feb 15 04:50:27 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit dc5e1979dce95f83e4ec571c2e1ffb166808a615[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Feb 15 04:36:54 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 4d9a07da2d7869ff8f3a82d04578581f3cea478b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 14 21:28:54 2017 -0800

    Fixed parsing problem with Calcasieu Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LACalcasieuParishParser.java
M	cadpage-private

[33mcommit 1c2b8f9f6a4b3cb16a1309785e1ea08054f906f3[m
Merge: 43f671a bf33741
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 14 21:27:14 2017 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers
    
    Conflicts:
    	cadpage-private

[33mcommit 43f671a50fec0b2dce96648d7e6d1e858aa07764[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 14 21:24:09 2017 -0800

    Release v1.9.11-24

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit bf337418367b70e6b6beec3ebe3594bc362bbfc3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Feb 14 03:06:32 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 0439d4ce44c92801c1b2f77fad04204b53415770[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Feb 14 02:13:08 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 87aadffcd7618a9a377fad66780267f4b53365aa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 13 15:30:36 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyParser.java
M	cadpage-private

[33mcommit 65f73df11b832556b4fe7699cbe309a3c136d751[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 13 14:54:36 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-private

[33mcommit ad7186c3846cf2ac0adc212ce4e0be9e6c9ae1a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 13 13:33:13 2017 -0800

    FIxed parsing problem with Marshall County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarshallCountyParser.java
M	cadpage-private

[33mcommit 0544786c5a10b9807850c9ad29c4184c5367eacd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 13 12:56:58 2017 -0800

    Fixed parsing problem with Morris County, NJ (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit bd39bdd2c877788f26e9e575caaff36b3f97208b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 13 09:56:14 2017 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java

[33mcommit 0c6bcf9cc678878e2a2c69e43e01e761e52bb075[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 13 09:52:15 2017 -0800

    Update sender filter for Lancaster County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
M	cadpage-private

[33mcommit 64ffc79dcb74f6d893d0203b2c88c7202e7a244d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Feb 12 01:36:23 2017 -0800

    general updates.

M	cadpage-private

[33mcommit eb7799bca2d532b9e103b405a42b9df447679290[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Feb 12 01:27:56 2017 -0800

    general updates.

M	docs/support.txt

[33mcommit eb9a96bc3c1206adcc736b6f3c36b0a036f5dbbd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 10 22:15:11 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 854e48ee125bdec044c64cccad3e42565cdcf8fb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 10 21:55:34 2017 -0800

    Fixed parsing problem with Colbert COunty, AL (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALColbertCountyBParser.java
M	cadpage-private

[33mcommit 45fbe051fa69b734ff183cf9aa0beefa0de811b4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 10 14:10:09 2017 -0800

    Checking in Medina County, OH (C)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyGParser.java
M	cadpage-private

[33mcommit 631d4a37431bec9003656ac87fff45f49bf29340[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 10 09:49:42 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMarlboroCountyParser.java
M	cadpage-private

[33mcommit b90de318e6f4110080131c5866acaa225ee527a4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 10 08:43:29 2017 -0800

    Update A911 parser table

M	cadpage

[33mcommit 6ca50a75cbb49083363c83680a3c146ef7612722[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 10 08:38:13 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyFParser.java
M	cadpage-private

[33mcommit 7f8b8a191a68ca5867c94676b2aa30d527ec8031[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 9 22:05:35 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit a043b1b52c0b18f9b4fe4c5d158729e8771c9797[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 9 21:15:59 2017 -0800

    Fixed parsing problem with Adams County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COAdamsCountyParser.java
M	cadpage-private

[33mcommit f8ed72a3c873b511159bd6bf4b56116108a10cc0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 9 20:31:48 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA18Parser.java
M	cadpage-private

[33mcommit 26c2e7e216c36c3eefbc2545d2246c2ef611c7b3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 9 19:31:32 2017 -0800

    Fixed parsing problem with Williamson County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-private

[33mcommit 57c330da817d1adf8e74149d3f2f7b68aedc2f82[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Feb 9 02:17:25 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit ccf56b0560d7e5a09b3b474eb8be878b2bee5094[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 17:20:41 2017 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COArapahoeCountyParser.java
M	cadpage-private

[33mcommit 77a6ce1a768f6cf9f100c69e4f73426d91f7f6bd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 17:01:05 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 8cc3ef4bb123d4400d279fd3e306db257e2a6b81[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 10:37:07 2017 -0800

    release v1.9.11-23

M	build.gradle
M	cadpage

[33mcommit dd0f9a853a4c15d64991a11144dd08eb43b3c4fd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 10:09:25 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 4a629192713d1213a69ea951ef5e287de5fd6629[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 10:05:37 2017 -0800

    Fixed pasing problem with Cumberland County, NJ

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCumberlandCountyParser.java
M	cadpage-private

[33mcommit 879ce322815cfc565be097a3032216446b9597f2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 09:38:46 2017 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INTiptonCountyParser.java

[33mcommit edce6127ee1a1287ca1f972b3f6dd668d218c732[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 09:32:15 2017 -0800

    Update sender filter for Tipton County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INTiptonCountyParser.java
M	cadpage-private

[33mcommit 264071a0f02cc66f2d873f5e1fc846b2f19fae0c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 09:22:33 2017 -0800

    Ditto

M	cadpage-private

[33mcommit 29e1216871975da225152b355f6e6d9991cab2f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 8 06:32:44 2017 -0800

    Fixed parsing problem with Prince Georges County, MD (G)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyGParser.java
M	cadpage-private

[33mcommit 6964131bff0b14eceebea5d5e45a6151eec8b92c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 7 20:58:01 2017 -0800

    Fixed parsing problem with Allegheny County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-private

[33mcommit 69ae68dc7e01553d689217bb0b03c63a29f3777d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Feb 7 18:30:10 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 44e42625e8046c6e327c0080671976967d1a0105[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 7 08:26:06 2017 -0800

    Release v1.9.11-22

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 2c89b042b72ca79a6b27f13a8d38e0fd3857357a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Feb 7 04:33:55 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 289ae7af5049579293c23ad2e902742b3bbba0ed[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 22:53:09 2017 -0800

    Fixed parsing problem with Nueces County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNuecesCountyParser.java
M	cadpage-private

[33mcommit da130ef9b3374c028327e737a39af880ecedf13a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 22:33:12 2017 -0800

    Fixed parsing problem with Colbert County, AL (added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALColbertCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALColbertCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALColbertCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit dd386489017958168c15f158516cd5656d8ad3eb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 18:49:58 2017 -0800

    Fixed parsing problem with Ashlan County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyAParser.java
M	cadpage-private

[33mcommit 8494e41ac36e172560f82f86969f5003aa77d888[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 18:15:26 2017 -0800

    Added city table to Peoria County, IL (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILPeoriaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA49Parser.java
M	cadpage-private

[33mcommit 90a4dfcbe4f0cb243bdd2b8b095bc8526e1036ab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 10:43:49 2017 -0800

    Release v1.9.11-21

M	build.gradle
M	cadpage

[33mcommit e0be73bcaa6900dedcdea4ea86d1b0cb9582f105[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 10:09:05 2017 -0800

    Fixed more problems with Peoria County, IL (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILPeoriaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA49Parser.java
M	cadpage-private

[33mcommit 31179eb8086e7dce43afe5750eaf8e08c1ee7cf2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 08:58:58 2017 -0800

    Fixed parsing problem with Kern County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAKernCountyParser.java
M	cadpage-private

[33mcommit 47982a42394c2e6a20d66723e3b06e06d622eb06[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 05:03:11 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIRoscommonCountyParser.java
M	cadpage-private

[33mcommit 792e4df8247101cc2bf699c7259e362952141544[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 04:53:53 2017 -0800

    Fixed mapping problem with Auglaize County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAuglaizeCountyParser.java
M	cadpage-private

[33mcommit 4d2589a5c8268c0fb89e1fdda430707de14e2a48[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Feb 6 04:38:02 2017 -0800

    Fixed parsing problem with Montgomery County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyCParser.java
M	cadpage-private

[33mcommit 3bd62a3ec9ab27c620a2470ab8e0cfb023c66ac0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 22:59:38 2017 -0800

    Fixed parsing problem with Clinton County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClintonCountyParser.java
M	cadpage-private

[33mcommit b42a0160648ac6c90d54750c09618a38e5177ddf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 22:11:16 2017 -0800

    Fixed parsing problem with Woodbury County, IA (added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWoodburyCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWoodburyCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWoodburyCountyParser.java
M	cadpage-private

[33mcommit 11c5d6e1d4b58f8e6fe0bdc1c0dbd9dff7e6fbdf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 20:02:11 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit 33cc0ca89cf0c1233f07c60cc60cd287b582e9cd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 19:50:17 2017 -0800

    Fixed parsing problem with Linn County, OR (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyParser.java
M	cadpage-private

[33mcommit 76ed8e06c0ed2ab0e4ba36117763b268494e43c2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 19:02:03 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWarrenCountyBParser.java
M	cadpage-private

[33mcommit 805b6fa27c0640a3a3895b252c12e132e65dd3ca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 17:10:14 2017 -0800

    Parsing problem with McDowell County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMcDowellCountyParser.java
M	cadpage-private

[33mcommit 2b23f938f1eebfe33ffd955cb5882296d31596ba[m
Merge: e779369 427768d
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 17:10:06 2017 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 427768d3ca95369949514c3a7577b46516cb9301[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 16:54:44 2017 -0800

    Update parser stuf

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java

[33mcommit e77936913b7cb28a53a9796acad7376b9e547016[m
Merge: 89e1786 5bc7678
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 13:54:26 2017 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 5bc7678fb1173ca351012181cfc1c9013e4ec2d8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 13:51:27 2017 -0800

    release v1.9.11-20

M	build.gradle
M	cadpage

[33mcommit d560b5859aaa4473daf3d40b1e9b2d4959a46a8c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Feb 5 11:27:47 2017 -0800

    Fixed parsing problem with Terrebone Parish, LA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA13Parser.java
M	cadpage-private

[33mcommit 7d06e4bcd5288c529c9888dcec70a77764d45c9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 4 21:35:35 2017 -0800

    Fixed parsing problems with Centre County, PA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java
M	cadpage-private

[33mcommit ec09dd76a28307e49baff10f32e41520ce58687d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 4 19:10:47 2017 -0800

    Update sender filter for Marion County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit 89e17867c8ffe9839e3afc1b5977b45d5b5ebc6a[m
Merge: 28a6247 5d9642a
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Feb 4 11:50:32 2017 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 5d9642aa7bc22e55b1793975b30a736954985bca[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Feb 4 01:20:25 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 28a6247c9d858421dde7d9362369a692a8089894[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Feb 3 19:22:07 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 10b194db9b569b490ad4b8fd8a81fb0a2ed8f324[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 2 10:55:37 2017 -0800

    Release v1.9.11-19

M	build.gradle
M	cadpage

[33mcommit 624e75deb03babcafd946471298bf3275a2239db[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 2 10:18:17 2017 -0800

    Fixed parser test issues

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAYakimaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 0149f7b39b823e214bee60f1a94360d834bd1156[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 2 09:17:08 2017 -0800

    Fixed parsing problem with Yakima County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAYakimaCountyParser.java
M	cadpage-private

[33mcommit e9f69e9897addc8c33d7088c33d440705230a130[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Feb 2 07:53:14 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit c6c6b0cec2424037a573097ce807ae35d361ef68[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Feb 2 04:11:51 2017 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit 63eeeac7768d18d4235f196a421d0a479b6486fe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 1 22:51:55 2017 -0800

    Fixed parsing problem with Duplin County, NC

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDuplinCountyParser.java
M	cadpage-private

[33mcommit 5d9c7b91a20620a79d4b4d0d3b82827977ae98c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 1 22:36:51 2017 -0800

    Fixed parsing problem with Union County, OH

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MessageBuilder.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgInfo.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHUnionCountyParser.java
M	cadpage-private

[33mcommit 133ab29140f6dee03e4202b9bdb72175b81070e5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Feb 1 18:29:46 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f36610f6af1310982eafd69cac3aea189447dcc5[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Feb 1 02:49:16 2017 -0800

    general updates

M	cadpage-private

[33mcommit 41cc441072cb6f99e00842f46ba22f5f6846e60b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 31 21:38:57 2017 -0800

    Checking in genCallList script

A	scripts/genCallList

[33mcommit b997e7778bb6ba753a78cd2589022a81c40d20ca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 31 21:06:59 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWinnebagoCountyParser.java
M	cadpage-private

[33mcommit 0862935536f7f92345c45d6215ac974563f40b44[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 31 18:34:10 2017 -0800

    Release v1.9.11-19

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZSE/ZSESwedenParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA18Parser.java
M	cadpage-private

[33mcommit a6775df00def7e189ce6f62a98a995ac965c9412[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 31 03:27:44 2017 -0800

    -m

M	cadpage-private

[33mcommit 36d53ec58dc9004692db31fbf14bbd0076bcd0ff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 31 00:30:20 2017 -0800

    Fixed parsing problem with Sacramento County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASacramentoCountyParser.java
M	cadpage-private

[33mcommit 290dc34440d3a014d6ab4d2a4b49b7e69f46f21c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 31 00:06:56 2017 -0800

    Fixed parsing problem with Pitt County, NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyBParser.java
M	cadpage-private

[33mcommit 3b7bd4111b9871ba654ae7b8d6a61bca8355f482[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 30 15:17:37 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/RI/RIProvidenceCountyParser.java
M	cadpage-private

[33mcommit 5c8b6eab35b42157de83cc1c31893c9a2eed6713[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 30 14:59:15 2017 -0800

    Fixed parsing problem with Williamson County, TN (C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyParser.java
M	cadpage-private

[33mcommit ce59f1e77441dc9ecf5e79f0a78f4c7366a9b580[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 30 13:10:47 2017 -0800

    Fixed parsing problemw ith St Charles County, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStCharlesCountyParser.java
M	cadpage-private

[33mcommit d89cf5fd584a861e58e5c7c40164a1ab8caaffa3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 30 12:04:59 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXParkerCountyBParser.java
M	cadpage-private

[33mcommit f19cfd61b28801cd5c101d9826e4deebb3f14691[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 30 11:38:11 2017 -0800

    Addd Cuyahoga County, OH (F)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyFParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyParser.java
M	cadpage-private

[33mcommit f53f45c6fafaf7e3724e83d16f5950b3f96c0659[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 30 10:55:08 2017 -0800

    Fixed parsing problem with Parker County, TX (D)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXParkerCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA18Parser.java
M	cadpage-private

[33mcommit 2606b3d6c2494ac2fbd32c2ad6208b2044785dfc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 30 10:34:36 2017 -0800

    Fixed parsing problem with Albermarl County, VA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlbemarleCountyBParser.java
M	cadpage-private

[33mcommit 8d5f8f710df9822b167a7a463cb8d8c7a7359c7f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 29 12:16:40 2017 -0800

    fixed parsing problem with Nash County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CodeSet.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNashCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ReverseCodeSet.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TestCodeSet.java
M	cadpage-private

[33mcommit 013b40bb1b83c12c764958b611fd304474373353[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 28 19:14:16 2017 -0800

    update genome.log

M	cadpage-private

[33mcommit 199b4dc65ab0410f72039bedbf618a31acb5aea5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 27 20:24:54 2017 -0800

    Fixed problem with parser library build

M	cadpage-parsers/build.gradle

[33mcommit f49d767f3afa43d9d9bc9355140fe36c93747442[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 27 18:20:58 2017 -0800

    Release 1.9.11-17

M	build.gradle
M	cadpage

[33mcommit bc5cdb141a6ac5272e4e1318b7e2be1ab784e6f2[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jan 27 04:18:17 2017 -0800

    general updates.

M	cadpage-private

[33mcommit d4a84465fc9ef54aeb0db735b93b105b8774ee28[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jan 26 16:09:56 2017 -0800

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZMaricopaCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIIsabellaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTHillCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAUnionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMarlboroCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCoffeeCountyParser.java
M	cadpage-private

[33mcommit d41ab8f482211fc5853e0782024fd963b5ecfa03[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jan 26 14:50:23 2017 -0800

    addr check and skeletons

M	cadpage-private

[33mcommit 804371c393ab1f4f82658785c8cb0de6fee2f2c8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 25 15:47:55 2017 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java

[33mcommit 3845b3ffcc121bf4c61bc4b4115dd92b33377615[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 25 14:41:55 2017 -0800

    Fixed parsing problem with Centre County, PA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java
M	cadpage-private

[33mcommit 0d057d39685c7a45579eb5bab27a7a4f96ebb629[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 24 21:54:58 2017 -0800

    Release v1.9.11-16

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesCParser.java
M	cadpage-private

[33mcommit a953e43ceb5e44b8f7549f159cf205c9ebdd5014[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 24 17:08:29 2017 -0800

    Fixed the NSW, AU parsers

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesCParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUWhyallaParser.java
M	cadpage-private

[33mcommit 2fdec9dcbb53815e7a661414dae258f96f7fca84[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 24 10:13:46 2017 -0800

    Update stuff

M	cadpage-private

[33mcommit 22173bdd0b7d7255c23b565e5137a5aae055a083[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 24 09:53:40 2017 -0800

    update genome.log

M	cadpage-private

[33mcommit 4942697f4e5202101b43fb991f752b94939d9453[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 24 01:59:38 2017 -0800

    general updates.

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit b09303ddc3bdf3d2a1ffaac9848b7baf89a819fa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 23 20:23:43 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 8ba8de8617182f79741969b10e8c91ebd7d227df[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 23 17:14:00 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 51833850691dc324345ba1fb6a1e707e479ef16a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 23 14:39:15 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit df155dfc2115040ebf697a5246a17a6856f218f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 23 14:18:24 2017 -0800

    Clean up parser logic

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyBParser.java

[33mcommit 35a1d297a469f48a0b43f9b4d6ee498243b08413[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 23 12:53:48 2017 -0800

    Fixed parsing problems with Warren County, IA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWarrenCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA67Parser.java
M	cadpage-private

[33mcommit 420de366b85dcf06f64ea8e9f435d86372a2ceac[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 23 09:46:43 2017 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVCabellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA67Parser.java
M	cadpage-private

[33mcommit 2f8ed3db3fe90586925ca3ba18604a0111254a6c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 23 08:49:52 2017 -0800

    Fixed parsing problem with Cabell County, WV

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVCabellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA67Parser.java
M	cadpage-private

[33mcommit 6f7418e0c8ba6ae1d781766a0034ad0631982e0a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jan 23 01:46:42 2017 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit 8a3d4a79c9a3a59eaee7d9d4e87385bf6e003daf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 22 12:20:11 2017 -0800

    Fixed parsing problem with Orangeburg County, SC (added B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMontezumaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOrangeburgCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOrangeburgCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOrangeburgCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA67Parser.java
M	cadpage-private

[33mcommit 9aa49ee8009df890b0e0a448fdeb069e52eb0f9c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 21 17:55:44 2017 -0800

    Release v1.9.11-15

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALakeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAllParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyFParser.java
D	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyIParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA14Parser.java
M	cadpage-private

[33mcommit f015c453ddc8db1305c95c662ccc7bb978bd9ab6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 23:44:36 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 35128e7dbd860e376393524949fec7fe72d910be[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 22:08:30 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 1518f379f3db09679d76a36325905e6b0ab4750e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 21:54:11 2017 -0800

    Fixed parsing problem with Arlington County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAArlingtonCountyParser.java
M	cadpage-private

[33mcommit ee0c92e86e54452eb32e6739d548ff69b2519f4c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 21:09:32 2017 -0800

    Fixed parsing problem with Mendocino County, CA (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMendocinoCountyAParser.java
M	cadpage-private

[33mcommit 76a2a040563440c3dbb0bdbc0b8678d3ead9f3cb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 16:57:36 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALakeCountyParser.java
M	cadpage-private

[33mcommit 434462b2b27ba7e9a82a296251692f773cde67b4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 16:40:53 2017 -0800

    Fixed parsing problem with Ashland County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyAParser.java
M	cadpage-private

[33mcommit 532d18e08f77091b467b2cb222552257d5beeae8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 15:39:05 2017 -0800

    Fixed parsing problem with Sussex County, NJ (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyAParser.java
M	cadpage-private

[33mcommit 8868237e00f08cdd80741fb99e35cda8e9888369[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 15:02:05 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBlountCountyBParser.java
M	cadpage-private

[33mcommit b7ac9670bb71ec48b186b9678f3602630a806abf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 20 14:23:37 2017 -0800

    Fixed parsing problem with Montcalm County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMontcalmCountyParser.java
M	cadpage-private

[33mcommit ea6c80927903961fbc3c2deb21e43bc46b194c31[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jan 20 03:30:26 2017 -0800

    general updates.

M	docs/support.txt

[33mcommit c809a3970ae045e75c4085b4f00cd95ea0e7f978[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jan 19 03:20:43 2017 -0800

    general updates

M	docs/support.txt

[33mcommit b5a30bc790d361febab1ec29306387578e8b7614[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 18 23:22:18 2017 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit afb27a8f98a087ec480ea58b89f35a796c837dab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 18 21:01:52 2017 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWinnebagoCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCABC/ZCABCMidIslandRegionParser.java
M	cadpage-private

[33mcommit 55c7e584bb79bf37c5f8f7f27d2a54ec25ca1a63[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 18 20:38:29 2017 -0800

    update genome.log

M	cadpage-private

[33mcommit 51818b8209c40e83ea0172ede806ee63346993e5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 18 20:25:27 2017 -0800

    Fixed parsing problem with Suffolk County, NY (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyBParser.java
M	cadpage-private

[33mcommit fa0aa16cde7a9ca91f9b42a8316c186abd5e8514[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 17 21:05:31 2017 -0800

    Parsing problem with Suffolk County, NY (F)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyFParser.java
M	cadpage-private

[33mcommit fbf3bfdc8b680b7a75ee4bf50951d01d9c589e05[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 17 15:31:05 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit cb54f1fca21d7eb08ffe9d16d872af33f99f7a18[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 17 15:27:28 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJGloucesterCountyAParser.java
M	cadpage-private

[33mcommit 86c259ff0232b1593d7b99fc8441bd9c558e82b4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 17 11:44:51 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
M	cadpage-private

[33mcommit 06c7e3d986980e182dbbf65e6e5e67877fa7987f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 17 11:29:00 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 4c3267a75d5eef582eb4614e09d10db5842c77f8[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Jan 17 00:49:03 2017 -0800

    general updates.

M	docs/support.txt

[33mcommit b14eb585f108d4aea45d0212301738f24b5384a5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 16 14:17:48 2017 -0800

    Fixed parsing problem with Monroe County, NY (Webster)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMonroeCountyWebsterParser.java
M	cadpage-private

[33mcommit f3de360365e3874c492c683d289868a3c8ac59f5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 16 12:55:23 2017 -0800

    Fixed parsing problem with Cabarrus County, NC (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCabarrusCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCabarrusCountyBParser.java
M	cadpage-private

[33mcommit 1797868ed2815bc1b5f62025ca5683356c220d4c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 16 12:27:20 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit b46245f838d784d94ba34cf4f69e426e1fcb680f[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jan 16 02:33:00 2017 -0800

    general updates.

M	cadpage-private

[33mcommit 18817c4a655c3e9d1d3b1bbc05528ad876e7a978[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Jan 15 13:06:11 2017 -0800

    general updates

M	cadpage-private

[33mcommit b09939f573d69c60bd8b572919a3f7cfb3a4f37d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 15 08:52:47 2017 -0800

    Update Active911 active user list

M	cadpage

[33mcommit 9e0e395a93ef672e194fce3ec66f2a316de53c9f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 19:21:16 2017 -0800

    Release 1.9.11-14

M	build.gradle
M	cadpage

[33mcommit 7b0434d99be38d510d56114dd69c184cfff8892e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 17:13:50 2017 -0800

    Fix test failures

M	cadpage-private

[33mcommit 1647f047dd5c16c71791b6f9eb8a20dd653bbbe3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 16:57:13 2017 -0800

    update genome.log

M	cadpage-private

[33mcommit ddd0518c23f312bf847f1e9e8f56f37b674bd5d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 16:28:15 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSSumnerCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMorganCountyParser.java
M	cadpage-private

[33mcommit 1da03f6c4b39e42bdc5adef2f6199bcece9fba25[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 15:24:54 2017 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java
M	cadpage-private

[33mcommit 858cdfd43b4dde9c70764fbb2a629a41d5a12c44[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 15:09:20 2017 -0800

    Fixed parsing problem with Centre County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java
M	cadpage-private

[33mcommit 38564689ac507710a3d7da3b16f08397ac3a4bca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 14:17:00 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 79c9d03a1bf0a121116e71d029aabd2029b4c673[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 13:58:19 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit fc0427542da04bea9d7a3a3d14e31739d6c5b513[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 13 12:15:43 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit 1702a2d9ce3dd6d8bc3dedd89ff62f61172e1e0e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jan 13 01:30:48 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 9edbaca8e0ac48fe92bf8a46858120c4a6a91df2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 12 22:34:23 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 835bde5ab47b4e71167d46628430c9ebbc6dde97[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Jan 12 21:25:32 2017 -0800

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWapelloCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWinnebagoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSSumnerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMorganCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyBParser.java
M	cadpage-private

[33mcommit e8c959b2cc11cc5c006294d83edf385620e69196[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 12 09:03:15 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 0febbc466456040ad6d26f2270e396b74d220192[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 19:00:45 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit f074de513353967803d13430df980d3ca3716ad7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 16:32:26 2017 -0800

    Checking in Medina County, OH (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA55Parser.java
M	cadpage-private

[33mcommit d43e8db2f42f43031dc38ded803a28121556eacd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Jan 11 15:55:34 2017 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit b116c89c1d2da93f401e22290644787bfaa316e6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 15:46:53 2017 -0800

    Checking in Knox County, KY

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYKnoxCountyParser.java
M	cadpage-private

[33mcommit c8ebd26929c1d742a516b434d25823e96de06929[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 15:07:46 2017 -0800

    Checking in Isabella County, MI (B)

M	cadpage-private

[33mcommit c56ac8228a827a0a8717d821e2d5791658ca1475[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 14:47:46 2017 -0800

    Checking in Franklin County, VT

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTFranklinCountyParser.java
M	cadpage-private

[33mcommit b551738b489e6d172d10cb02ea5e391d8aca02fa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 13:55:34 2017 -0800

    Checking in Union County, PA

M	cadpage-private

[33mcommit e5c3abab43b65774c07f2585e48d66a0e055710f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 13:38:53 2017 -0800

    Checking in Hancock County, IA

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAHancockCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA47Parser.java
M	cadpage-private

[33mcommit ef4dca6188b5c5eee64b3a4f5e4ff0dca8d6f41c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 11 12:03:56 2017 -0800

    Checking in new parsers

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COPitkinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCBerkeleyCountyParser.java
M	cadpage-private

[33mcommit ed547f3767a041ddb296106faaa8f9e04080fc76[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 18:58:34 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit ebdafafe2738c48276085a3c71ff34d5f496f413[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 18:41:16 2017 -0800

    Fixed parsing problem with Poplar Bluff

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPoplarBluffParser.java
M	cadpage-private

[33mcommit c551db37ba1dc264b045eac3c03d5104da178e78[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 18:15:41 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 3fc2630e0c7d75b23feca7adf915c3bba3837d2f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 18:10:43 2017 -0800

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 394628ff7b10b8ee098030b18a0df7220b39bf8f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 14:07:33 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit b85d0fc873b32d1152aeeaeafe0309de8278fc1d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 13:53:49 2017 -0800

    Update sender filter for Cumberland County,NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCumberlandCountyParser.java
M	cadpage-private

[33mcommit 90fe4cef96360891bc45e354428d020485e7522b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 13:10:51 2017 -0800

    update genome.log

M	cadpage-private

[33mcommit c087fea1e2a4539f6e50de51c972ada4e2509e81[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Jan 10 13:07:30 2017 -0800

    Fixed parsing problem with Augusta County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAugustaCountyParser.java
M	cadpage-private

[33mcommit b5f8f16094c6ad68f1c9bda63cb428139975230b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jan 9 04:10:35 2017 -0800

    general updates

M	cadpage-private

[33mcommit ddbc9c25fad0987f0c079a891a54da1b979fa182[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Sun Jan 8 15:13:56 2017 -0800

    parsers

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYKnoxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXEllisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTAddisonCountyBParser.java
M	cadpage-private

[33mcommit 096a9406866a256ad65d86e2eab86ddf300701ef[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Sun Jan 8 14:03:54 2017 -0800

    skeletons and addr checks

M	cadpage-private

[33mcommit b5aeabc7502e4742859bdd73b89cee4d97f49af7[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Jan 8 03:32:46 2017 -0800

    general updates.

M	cadpage-private

[33mcommit f307d8b29191729c1e270080c46089034ff133e9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 20:22:14 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit b644d8ef9c974b9012ffc7ab89598f529aa92e93[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 19:41:55 2017 -0800

    Fixed parsing problem with Harris County, TX (ESD1B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyESD1BParser.java
M	cadpage-private

[33mcommit 655c47a7be6264d640f31a4e1f7f94f4c7db16c8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 17:53:40 2017 -0800

    Fixed parsing problem with Newberry County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCNewberryCountyParser.java
M	cadpage-private

[33mcommit 6aea60cabbabe2d39356eaf1ea2af535523e100f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 12:57:52 2017 -0800

    update genome.log

M	cadpage-private

[33mcommit f9fd2da21b856ec43b70d360a882e9f16e3eac4c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 12:40:50 2017 -0800

    Fixed parsing problm with Mansfield, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMansfieldParser.java
M	cadpage-private

[33mcommit 73b43e3af282e4b6614b7523e1b72d207d89e140[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 11:51:58 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit e147cf1e8799670889cb7788f8e4d4fc5a1173de[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 11:43:27 2017 -0800

    Fixed parsing problem with Place County, CA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyBParser.java
M	cadpage-private

[33mcommit 11cf20c4954bfbc456ce22a115d2be26980d4d2e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 11:40:19 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit 015cbf59d7e70b6aad20bceb5d8ef42a18f33c31[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 11:35:01 2017 -0800

    Fixed parsing problem with Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit 6252213335b8519d126133b8809b0491abba858b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 11:08:37 2017 -0800

    update msg doc

M	cadpage-private

[33mcommit b2b78d0e94f4c2d37e3729d30380e91ea3d16ebe[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Jan 7 10:39:29 2017 -0800

    Fixed parsing problem with Morgan County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMorganCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA27Parser.java
M	cadpage-private

[33mcommit d53c4eaf49f2b982bf470260a8112871ad6f98db[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 6 21:26:43 2017 -0800

    Update genome.log

M	cadpage-private

[33mcommit bd0faf3c0f3d237ccb5f44876b5369c8711ebaf3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Jan 6 18:54:06 2017 -0800

    Fixed Parsing problem with Somerset County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyParser.java
M	cadpage-private

[33mcommit 24bc2d71d0344d6f39e030e196b13cce2f5aba24[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Jan 6 03:16:43 2017 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit e08a606dd05a43f0a52d9c6193868fa3d8dbf8ee[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Jan 5 09:06:58 2017 -0800

    Release v1.9.11-13

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJHunterdonCountyParser.java
M	cadpage-private

[33mcommit 151bfb653bb1ca09ef129c8c07fb80d9dc29a326[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Jan 5 04:21:36 2017 -0800

    general updates.

M	cadpage-private

[33mcommit b51816f7cd4942acc6910cf35b41f4fa1b621957[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Jan 4 21:33:40 2017 -0800

    Fixed parsing problem with Hunterdon County, NJ

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJHunterdonCountyParser.java
M	cadpage-private

[33mcommit 22b0c1615e21fb91b55683df59fa882bc01e459e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Jan 2 23:53:45 2017 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit e507e0369397ff026075aa5dc7140b4b6a36eac8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 2 12:48:41 2017 -0800

    update genome.log

M	cadpage-private

[33mcommit b9bae33552e4642d67a3c3a344d012511ad16964[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 2 12:44:22 2017 -0800

    Fixed parsing problem with Hamilton County, IN (A&B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
M	cadpage-private

[33mcommit 3ca5b6b37201afd295e99dd60e9d1b19088c724b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Jan 2 11:28:13 2017 -0800

    Fixed parsing problem with Orangeburg County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOrangeburgCountyParser.java
M	cadpage-private

[33mcommit 4d4915dfa1ba6417760c871911e5426b70ba2afa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 1 17:55:57 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 05372734e824eb5325859d20651ec7e2ee464da5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 1 14:39:00 2017 -0800

    Update msg doc

M	cadpage-private

[33mcommit 0fa6733802ce8ab916dd6ba3a0deb8634a0089a2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 1 13:58:32 2017 -0800

    Fixed parsing problem with Trenton, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHTrentonParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 1eed8ab8a5657c1163f7a41ca75ecdd68a48c133[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 1 12:22:29 2017 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTollandCountyAParser.java
M	cadpage-private

[33mcommit 0f3c3ef772b6dafe119ffb9b170246c662d37d76[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Jan 1 11:47:15 2017 -0800

    Release v1.9.11-12

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 203ef476e4004fbf92da36ef3867f6e53625fed1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 31 19:25:45 2016 -0800

    Fixed parsing problem with Preble County, OH

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
M	cadpage-private

[33mcommit 0393ad1dc53a1b9c1018870bb54836ccff090acb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 31 17:48:46 2016 -0800

    Fixed parsing problem with Granville County, NC
    Reworked everything about the DispatchSOuthernParser base class

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyHParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAnsonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAsheCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCarteretCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCChathamCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDuplinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCFranklinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGranvilleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMooreCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPolkCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWarrenCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWataugaCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCFairfieldCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNJeffersonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKaufmanCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRoyseCityParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 44fac538914bbbf0039c412e367ff7e3829de646[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 31 17:30:00 2016 -0800

    update genome.log

M	cadpage-private

[33mcommit bbd0b69af42e7664c4ccb09054f49b7285910597[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 31 16:55:30 2016 -0800

    update msg doc

M	cadpage-private

[33mcommit 0de83bc5e0a410756917a75c0eed8c54cee6bfb8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 30 17:28:17 2016 -0800

    update genome.log

M	cadpage-private

[33mcommit d67bdae057665b19694131211f7cc2959a796101[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 21:05:00 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 3283769e0caa388e7bc5ee1e952553defce29f5a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 21:00:12 2016 -0800

    update sender filter

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMcCormickCountyParser.java
M	cadpage-private

[33mcommit 8ed6dea088d560ef5d332e4f4791bcb774b17066[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 20:27:36 2016 -0800

    update stuff

M	cadpage-private

[33mcommit 13585b3219c66527ed5b354d503ee808d525a821[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 20:17:29 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyAParser.java
M	cadpage-private

[33mcommit 6eac2d8c4d470f761a3ddb7b754823ef60ef743a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 20:00:17 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit dc5b67887eeca95fc19c3378af53a13b915985a6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 19:33:52 2016 -0800

    Checking in new StatusPanelDevice parser

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/StatusPanelDeviceParser.java

[33mcommit 9f87b356574df31d758e89fbc45154ce7cb47a03[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 10:46:45 2016 -0800

    Fixed parsing problem witih Caldwell County, NC

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCaldwellCountyParser.java
M	cadpage-private

[33mcommit 88215a7735449237c50e29e19eac9a6ae5c6b625[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 09:30:10 2016 -0800

    update msg doc

M	cadpage-private

[33mcommit af00becf1f15d80b17c76e26d939693affaa30bc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 29 08:41:28 2016 -0800

    update msg doc

M	cadpage-private

[33mcommit 943f015cad217fb6ee1907370871c1ac699a4773[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Dec 29 02:46:06 2016 -0800

    general updates.

M	cadpage-private

[33mcommit 203197719356fbe82aed9f48927570b327f107c4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 28 09:42:01 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit 107163552a239c99362ee4d7c0175f5ce2360eb9[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Dec 28 03:11:44 2016 -0800

    general updates.

M	cadpage-private

[33mcommit ed0370e4d677d4762bd5267d74f958d6af1fa176[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 27 17:50:48 2016 -0800

    Fixed parsing problem with Warren County, IA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWarrenCountyBParser.java
M	cadpage-private

[33mcommit 1b0f027b575b5744dbe112a0ca591dcfa55abc82[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 27 15:24:50 2016 -0800

    update sender filter for Henry County, GA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAHenryCountyParser.java
M	cadpage-private

[33mcommit 4113dbbc079bc6b8929569affe5676e3de44e101[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 27 12:28:58 2016 -0800

    Restore old tests

M	cadpage-private

[33mcommit 5fa8375a2cd05c0928d4d325410707f4a5d145d8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 27 12:23:10 2016 -0800

    Fixed parsing problem with Lane County, OR

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLaneCountyAParser.java
M	cadpage-private

[33mcommit 6f1e8042dec3e560dffb4c2c49d1dc396a7f72cb[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Dec 27 05:24:13 2016 -0800

    general updates

M	cadpage-private

[33mcommit 02230bc29128dc97b52870274d54253fedad5f7c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 26 14:26:32 2016 -0800

    update city code tables for Du Page County, IL (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyAParser.java
M	cadpage-private

[33mcommit f7fb1cbb0ce38161ae2c09a1359556a60aa8ae75[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 26 14:05:01 2016 -0800

    Fixed parsing problem witih Sheboygan County, WI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WISheboyganCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA19Parser.java
M	cadpage-private

[33mcommit cb4dab0e745571907e566237375eebeab8b87023[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 26 11:07:28 2016 -0800

    Checking in Berkeley County, SC

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStCharlesParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRoseauCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHydeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYGreeneCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCBerkeleyCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCFlorenceCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBParser.java
M	cadpage-private

[33mcommit 15c27fde4245e8db3aa769fefa1062132b28026d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 25 10:44:59 2016 -0800

    Fixed parsing problem with Dodge County MN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDodgeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA27Parser.java
M	cadpage-private

[33mcommit 1ac82e9da25e0102ea16e5f2b88004297f7a724a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Dec 24 04:26:32 2016 -0800

    general updates.

M	cadpage-private

[33mcommit 6b1b7176d32b79073f0983630695d231dbd88852[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 23:25:17 2016 -0800

    Update A911 parser table

M	cadpage

[33mcommit 81cc53a6c5e0ab4e2c684969ada1c15f3e78ecdd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 23:17:42 2016 -0800

    Checking in Caldwell County, MO

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCaldwellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBCParser.java
M	cadpage-private

[33mcommit 584da168e217090249c1b32668d24619e15a8887[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 22:12:49 2016 -0800

    Checking in Pitkin County, CL

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COPitkinCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA19Parser.java
M	cadpage-private

[33mcommit 797b4639acc61730b383bcdbc043c971e6916f69[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 21:27:14 2016 -0800

    Fix parse failures

M	cadpage-private

[33mcommit 71aaa6a2496fd0289e66b3e7cbf80950aec04991[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 20:58:40 2016 -0800

    Checking in Maries County, MO

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA25Parser.java
M	cadpage-private

[33mcommit 1cfd86bef3e7244f5d4415dfc29d75e7284ea744[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 14:43:57 2016 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGuilfordCountyParser.java
M	cadpage-private

[33mcommit a08b23bd61d2fd27ed25ad5b863a0be8377d1779[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 14:18:50 2016 -0800

    Checking in Wright County, MO

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOWrightCountyParser.java
M	cadpage-private

[33mcommit 2771d5914ef4d82b20b2799f5b72701a371af94e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 23 13:49:23 2016 -0800

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit 27ff1e589cbea0c9a31ad2a627725e812dd52a22[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 22 20:02:24 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit 94ee72d7bdf62620303c091bec9f18b2747868ab[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 22 20:00:29 2016 -0800

    Fixed parsing problem with Cass County, ND

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
M	cadpage-private

[33mcommit 6aea226001499be7d4e0cdbb96fd74239f0e102c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 22 19:28:34 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit edfe600042acc7160d92b2312ecf795dcef5e27a[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Dec 22 16:25:47 2016 -0800

    skeletons and parder

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCBerkeleyCountyParser.java
M	cadpage-private

[33mcommit e515507f55ed6767176a16f48e40b0d1de3f5a34[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 22 11:52:48 2016 -0800

    release v1.9.11-11

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 0f788f59ced2c416deb29ff46ebd6e2f6ed47b92[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 22 11:14:17 2016 -0800

    Fixed parsing problem with Montgomery County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMontgomeryCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit ffc06b96e1fa0c18e5fb2f5a83c702600c7ce88d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Dec 22 00:06:26 2016 -0800

    general updates.

M	cadpage-private

[33mcommit 0c8760c6e4fb2815c25a5867786d91a9fb11dab5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 20:23:05 2016 -0800

    Added Limestone County, AL (B)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLimestoneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLimestoneCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLimestoneCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMadisonCountyParser.java
M	cadpage-private

[33mcommit 103eb71953ef63bc51e08a62f93854f789d5759e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 17:59:27 2016 -0800

    Parsing problem with Kane County, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyParser.java
M	cadpage-private

[33mcommit 62f46c4ea725c54b3086e64bd09802fc48e1864a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 17:30:11 2016 -0800

    Fixed parsing problem with Clarion County, pA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyEParser.java
M	cadpage-private

[33mcommit d0a81ca05c108decaad266bf14253bfcaf689e29[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 12:07:45 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit c3e3b394e6e5a67aa5d5f8cea9f21b03b9cd3b7a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 11:43:05 2016 -0800

    Fixed parsing poblem with Manatee County, FL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLManateeCountyParser.java
M	cadpage-private

[33mcommit 11ad48fca3eefbb1c9f78709ced1653b919116c9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 10:34:39 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 82712d94cd04c17e87511d6266772c214ac281f8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 10:18:56 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 6ae132f1c729ebde5b88475318c8294e317688f7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 09:53:20 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLaPorteParser.java
M	cadpage-private

[33mcommit ae18eff0634162f09f0f6140041157df43ad8a80[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 09:38:53 2016 -0800

    Update unit pattern for Marion County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit 3fc59087ad49acd1d8c256f24bbb2aaa6832b8c7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 21 09:20:27 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 8b4fe58ef13c30c2938f87e8ac32d1db87c5812e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Dec 21 01:25:00 2016 -0800

    general updates.

M	cadpage-private

[33mcommit 9b5813dae07fa33386f888bcc316beacebb8a2ec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 20 21:38:52 2016 -0800

    Update sender filter for Ashland County, OH (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyAParser.java
M	cadpage-private

[33mcommit 77f087eb212a12bc8434914f16eda17a8fc39c26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 20 21:28:48 2016 -0800

    Parsing problem with Cass County, ND

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
M	cadpage-private

[33mcommit 0dd6803d33ff82a0b22ee9d34426fb983d23112b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 20 16:16:27 2016 -0800

    Fixed parsing problem with Carroll County, MD (A)

M	cadpage
M	cadpage-private

[33mcommit 4f70bfbc2534aa402eedac0ff91a09c5e41d18bf[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 20 11:05:08 2016 -0800

    Fixed parsing problem with Albemarle County, VA (B&C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlbemarleCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlbemarleCountyCParser.java
M	cadpage-private

[33mcommit b7d80cac56700fad81acf10f07e1b1ff5bb5faef[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Dec 19 23:35:39 2016 -0800

    general updates.

M	cadpage-private

[33mcommit 5062c44b8aa20511d2830f2f6f3d4152e76aded1[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Dec 19 14:58:21 2016 -0800

    general updates

M	cadpage-private

[33mcommit 1f38a53ac6a9eed0df7b6ade90414a48ca13e896[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 19 12:43:27 2016 -0800

    Release 1.9.11-10

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyGParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyHParser.java
M	cadpage-private

[33mcommit 73e1265f59fd3b4be227c030ee055fe2c9502e50[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 19 05:25:12 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit fcba796af929922e170e86f36daebe920d525f4e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 21:43:38 2016 -0800

    Rename parser

M	cadpage-private

[33mcommit 3636be747d583b66ab7893e8e34e2445e78987f8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 17:55:31 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit d084e802dd6b2d9a017901a48b6bee969208f319[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 17:54:22 2016 -0800

    Fixed parsing problem with Sedgwick County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSSedgwickCountyParser.java
M	cadpage-private

[33mcommit bab3e9584d69f00edb6e70936fb6c1a095bd78f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 16:15:20 2016 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMissouriCityParser.java
M	cadpage-private

[33mcommit 3d7f3cf27909221391a604ea2714c57e3cdd9c4d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 15:57:28 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit ebaf2afdbeaed005ee6ddc079e0f635fd3b505da[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 14:56:21 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit ed1d712891fb9dcbeeb5bcd9034986f233cd337e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 14:05:49 2016 -0800

    Fixed parsing problem with Hillsdale County, MI

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIHillsdaleCountyParser.java
M	cadpage-private

[33mcommit 9ed0e26c0682114984965caf97c70f7ffe27982f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 10:35:28 2016 -0800

    Fixed parsing problem with Prince Georges County, MD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyFParser.java
M	cadpage-private

[33mcommit 095757568e7b14b1ba1340d4ddb0375ca6e74356[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 10:16:24 2016 -0800

    Fixed parsing problem with Jefferson County, AL (G)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyGParser.java
M	cadpage-private

[33mcommit bdb7bdcfdbd0d41f8e2b969b017468aa427ef039[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 18 09:16:58 2016 -0800

    Fixed parsing problem with Rutherford County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRutherfordCountyParser.java
M	cadpage-private

[33mcommit 1bf736197fbb8530f9c1058bf63aba0a4a6b6269[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 17 18:17:41 2016 -0800

    Fixed parsing problem with Botetourt County, VA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABotetourtCountyParser.java
M	cadpage-private

[33mcommit 029ab8c8fb52bb6c627e7b57a95ef3e9d901ec08[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 16 20:38:06 2016 -0800

    Update A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyFParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyNParser.java
M	cadpage-private

[33mcommit ac6ff43c95a4e971058c57ece24ed434e1db4c25[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Dec 16 03:55:46 2016 -0800

    general updates.

M	cadpage-private

[33mcommit e56e10fab02024755c3b652600049546e2bb96ec[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 16 00:10:48 2016 -0800

    Fixed parsing problem with Montgomery County, OH (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyAParser.java
M	cadpage-private

[33mcommit 7bc23e574f7a9a5c3a0662a5153cb4355f7bdadc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 15 23:38:54 2016 -0800

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java

[33mcommit 014facdc003fdf6be402322a93027fc72a1f906f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 15 23:38:01 2016 -0800

    Fixed parsing problem witih Cass County, ND

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
M	cadpage-private

[33mcommit 8da94e1e229426068b5bf606e4e9b92f65e92aac[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Dec 15 01:40:31 2016 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit fb627a2420f2644736833f23845abc5e4d325170[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 14 08:27:47 2016 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/RI/RIProvidenceCountyParser.java
M	cadpage-private

[33mcommit 7859fcf62e681459b8212e08e31fd096969472a1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 13 15:45:44 2016 -0800

    Release v1.9.11-09

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHHanoverParser.java
M	cadpage-private

[33mcommit a7b10d5fdc85abf1948b6f30f811dab92663289d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 13 13:18:01 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit fa84088a1f3db70b93a5fd6b8b2c64eb12cce2d0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 13 11:22:32 2016 -0800

    Fixed parsing problem with Pitt County, NC (Added B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyParser.java
M	cadpage-private

[33mcommit 62f0dfedb345ad40df36c1a1138f6efede0a708f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 12 12:20:14 2016 -0800

    Update A911 parser table

M	cadpage

[33mcommit 28699b10d9b3ed091a4569b040e9cc995e2b7b0b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Dec 12 02:45:19 2016 -0800

    general updates.

M	cadpage-private
M	docs/support.txt

[33mcommit 070d4b2177f782ee5ef6044a4284b7537b948cf3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 11 19:43:01 2016 -0800

    New parser: CAStanslausCountyB

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanJoaquinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAStanislausCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAStanislausCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAStanislausCountyParser.java
M	cadpage-private

[33mcommit f1802bb0d396dc66f9b029d3ebce758aacd4a605[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 11 18:32:43 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 1e086a35c14883707c0f703e5d150cfec98484a6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 11 18:00:35 2016 -0800

    Fixed parsing problem with Robeson County,  NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRobesonCountyParser.java
M	cadpage-private

[33mcommit 549d39c339ef723eb51a8186d800a8c42238fc5a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 11 17:38:23 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 4816f8cd2df449b28b2e1ed41c88ddb5d13c062c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 11 13:59:19 2016 -0800

    Parsing problem with Pierce County, WA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit c27ad9217065b4c85e1a2620aa3db112e4dcb620[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 11 13:25:09 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 71a389a751bde58213d192945e9d51054d0e24cd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Dec 11 00:28:52 2016 -0800

    general updates

M	cadpage-private

[33mcommit 170b9773f524c8f33c3a57bf1ba6dd535302d05b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 10 02:41:31 2016 -0800

    Parsing problem with Clarion County, PA (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyEParser.java
M	cadpage-private

[33mcommit b7f3ed57a74b00c8197c70c285cea91579f80d56[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 20:19:42 2016 -0800

    update msg doc

M	cadpage-private

[33mcommit 58f777474ceeee71754fd30290328f39c7bca5ca[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 18:16:18 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTLamoilleCountyParser.java
M	cadpage-private

[33mcommit 6e972effb4e54b1a3b7f55fb915cec3ee6b4a8cd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 17:20:34 2016 -0800

    update msg doc

M	cadpage-private

[33mcommit 8246927eff3bbe863b90ff1a853f12df1dba6451[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 16:26:49 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyBParser.java
M	cadpage-private

[33mcommit 21aa2554d5ad2dd381761eee9a9512ec8a1a8d8a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 12:30:02 2016 -0800

    Relase v1.9.11-08

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 4c40027119a47a46c7a7cb275880e32b092e5037[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 11:02:49 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDGoodingCountyBParser.java
M	cadpage-private

[33mcommit 9a6768210d3723fdcc6e45f73ecbc455de8e1768[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 10:04:38 2016 -0800

    Fixed parsing problem with New Castle County, DE (E)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyEParser.java
M	cadpage-private

[33mcommit 86c87fdee8a8b5f9f273cb6ba80fff14c14ce603[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 9 08:31:56 2016 -0800

    Fixed parsing problem with Fairfix County, VA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyBParser.java
M	cadpage-private

[33mcommit 8e0e6fd2b5e85d7f273c6180db5a5150c94fcb2b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Dec 9 00:07:33 2016 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit a64cadd67b5dd4706598e5e464a088ccb818e550[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 8 21:42:36 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 4e054693c5a1afb4fddc8ae0f003be3696018765[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Dec 8 21:32:33 2016 -0800

    Fixed parsing problem with Lincoln County, MO
    Added MOPikeCountyB

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPikeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPikeCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPikeCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA25Parser.java
M	cadpage-private

[33mcommit 4980b6618536e9b66fdec4cc9b2395edb25f4974[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Dec 8 15:11:38 2016 -0800

    parsers

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COPitkinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INGibsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INNobleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHardinCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAAllenParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCaldwellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMariesCountyParser.java
M	cadpage-private

[33mcommit aa90c223de55192db2242be8055c92dbf9234a79[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Dec 8 00:01:09 2016 -0800

    general updates

M	cadpage-private

[33mcommit 64da625e6a337b87beb8a056ea3e850e9823ca02[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Wed Dec 7 19:21:47 2016 -0800

    skeletons

M	cadpage-private

[33mcommit e583abc7169c4458e9611dcca5e4d65f7c44d85d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 7 08:55:50 2016 -0800

    Fix text failure

M	cadpage-private

[33mcommit 73d4dcacaeef08e7f52f5781af1f93491cffc601[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Dec 7 07:52:55 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADinwiddieCountyParser.java
M	cadpage-private

[33mcommit 098b04a0ca61bbd53fe3e03410f16edd03ad2b18[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Dec 7 03:02:22 2016 -0800

    general updates

M	cadpage-private

[33mcommit 6a738a4e4ed81bd43e8302de297ee0d028364ddd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 6 21:15:01 2016 -0800

    Fixed parsing problem with Pender County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA3Parser.java
M	cadpage-private

[33mcommit a4211478082796ba2dcab778f3851623cd35f61d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 6 20:52:16 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 4195f34077aa0c58edeb6216ef7c09be0fe79355[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 6 20:38:11 2016 -0800

    Fixed parsing problem with Stillwater County, MT

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTStillwaterCountyParser.java
M	cadpage-private

[33mcommit 86f0ee658449ee437d7709ad6a414c363a3dfc20[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 6 20:07:08 2016 -0800

    Fixed parsing problem with Granville County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGranvilleCountyParser.java
M	cadpage-private

[33mcommit fac58a9ac998be14ddd847c19cf3098986532053[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 6 18:20:59 2016 -0800

    Fixed parsing problem with Westchester County, NY

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWestchesterCountyParser.java
M	cadpage-private

[33mcommit 39f3903c29729f725c95ac227d2008e21cbfd17d[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Dec 6 12:36:00 2016 -0800

    general updates

M	cadpage-private

[33mcommit 5a12e94cf6f59b1aa264056f00dc2bdc06b37475[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Dec 6 10:22:15 2016 -0800

    misc updates

M	cadpage
M	cadpage-private

[33mcommit 979a24dba2bac855cde0e8ad2bfd8eb74a1e4558[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Dec 5 23:38:29 2016 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit dacdd0037e94e312deb6a693f1572b6e752bd644[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 5 17:37:33 2016 -0800

    Fixed parsing problem with Weld County, CO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COWeldCountyParser.java
M	cadpage-private

[33mcommit b866ff025cfa1b5f9e8ffb69a7d333b14e12d6a0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Dec 5 10:45:08 2016 -0800

    Release v1.9.11-07

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
M	cadpage-private

[33mcommit cea32202a877cfebd3937adbca98522822acfa8a[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Dec 5 03:58:57 2016 -0800

    general updates

M	cadpage-private

[33mcommit 6b47f49a259799bb34fcf04a04da3b238b36bba7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 4 21:37:45 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit f376de98b3b3ce975a4eb8b76d3f2895346d095f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 4 21:33:21 2016 -0800

    Fixed parsing problem with Wayne County, PA (Added  B)

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWayneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWayneCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWayneCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
M	cadpage-private

[33mcommit e8788795f83a737e9c508e26cfc95d7d1d0f1829[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 4 19:38:36 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 1776e209cd3da6aa3fe69706afb79eaff9cd3f4f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 4 19:24:48 2016 -0800

    Fixed parsing problem with Wyadotte County, KS

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSWyandotteCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-private

[33mcommit 435bff336531181dc0a232dfd19390f7921a070d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 4 16:14:41 2016 -0800

    Ditto

M	cadpage-private

[33mcommit 528460a244dd71a0cc33bde0791cbc56de76a883[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 4 16:13:28 2016 -0800

    Fixed parsing problem with St Joseph County, IN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyParser.java
M	cadpage-private

[33mcommit 51dd22a6e5a39a5c0b1067abaa1705b82e4e439e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Dec 4 12:48:27 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCamdenCountyAParser.java
M	cadpage-private

[33mcommit d1e8f16a41aa4fd4d1882491ad6e560141641e02[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 18:57:08 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit 94f0cec94281a0e591c30f91801e7eabb2d4af26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 18:54:59 2016 -0800

    update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyBParser.java
M	cadpage-private

[33mcommit b88ae358711b1f7b43bf69b145029e3e0ec4aa08[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 18:39:08 2016 -0800

    Fixed bug in gsync script

M	scripts/gsync

[33mcommit 27ae6ef62fcc6516611c7d313dd5b76347185b16[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 18:34:55 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCollinCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA51Parser.java
M	cadpage-private

[33mcommit 9fd7fcda8a789bb79128da1debe1df3713295570[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 17:42:23 2016 -0800

    $1

M	cadpage-private

[33mcommit 859bf4318ae44dee1c199aa49812ba9a2d2a3fea[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 16:41:43 2016 -0800

    $1

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyParser.java
M	cadpage-private

[33mcommit 02bdada59415583d8b2fd9940a3daae625662c41[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 11:31:11 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit a2f050edddb7eb33d2c8f1e611ab22c662511dd7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Dec 3 10:27:56 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWestchesterCountyParser.java
M	cadpage-private

[33mcommit a4e1548a61ca6c18f238aad79c3f14a9e1c4c591[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Dec 3 03:59:16 2016 -0800

    general updates

M	cadpage-private

[33mcommit a7aac8b3ec70ad7744d6e63307baafaa58c5a972[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 2 21:13:46 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYoloCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit cfe471e6c92ad9600cdede33cde3a870c4e85952[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 2 20:25:35 2016 -0800

    $1

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyCParser.java
M	cadpage-private

[33mcommit 19f731816386fa492e51cb338e8465d541faf970[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 2 19:49:17 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyBaseParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD4Parser.java
M	cadpage-private

[33mcommit 0b0411199819267b2c1cb64f0ba71d56dcc5a227[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Dec 2 11:04:51 2016 -0800

    $1

M	.classpath
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStCharlesCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBuncombeCountyParser.java
M	cadpage-private

[33mcommit 31250d5088d0878306533ce6df013ba368502934[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Dec 2 03:01:49 2016 -0800

    general updates

M	cadpage-private

[33mcommit 8ca9a374dde93e3d6c0793db6efaf129eb0104ba[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Dec 2 00:44:46 2016 -0800

    general updates

M	cadpage-private
M	docs/support.txt

[33mcommit 9f38d9243e30b3faca8d1bac5a2eca4057f3b134[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Thu Dec 1 16:35:14 2016 -0800

    $1

M	cadpage-private

[33mcommit a92b8458bb01b8bf1886967f67ed4973eb1d059b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Thu Dec 1 00:40:44 2016 -0800

    general updates

M	cadpage-private

[33mcommit 2047b4a8d7f5693568a2dd217442314b4e777ea6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 30 08:59:25 2016 -0800

    $1

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 29e2db6b47fa20f5ac42ed5374f03e476ad0ac10[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Nov 30 03:50:41 2016 -0800

    general updates

M	cadpage-private

[33mcommit 68915ce1f8090f3493b9c69c3d8c3dce532d10d4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 29 19:26:58 2016 -0800

    $1

M	cadpage-private

[33mcommit d38886cb3533ca15c62894a7bfa4f104e2cb8555[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 29 18:35:40 2016 -0800

    $1

M	cadpage
M	cadpage-private

[33mcommit d86297767ce08fc4e14bd72b62a7278dfeabf12e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 29 18:21:00 2016 -0800

    $1

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COParkCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILTazewellCountyParser.java
M	cadpage-private

[33mcommit 8805b42ffd6d15542203ac731522eb224db0f990[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 29 17:06:52 2016 -0800

    $1

M	cadpage-private

[33mcommit f68387278521284c16a7e0a4af885563c88bdba7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 29 15:49:10 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGranvilleCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
M	cadpage-private

[33mcommit 865387f8f9c9e2aed1369359f49552cac73fdec5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 29 12:18:56 2016 -0800

    $1

M	cadpage-private

[33mcommit eec264d84c1a8fecc57720b997793ae6d6fac8c3[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Nov 29 02:52:39 2016 -0800

    general updates

M	cadpage-private

[33mcommit cad0a5b1ed788e9eb97409b0d8081a725530064a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 28 10:20:44 2016 -0800

    $1

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 6cb5ed1a3f5a58e433e36df4cba248f6560b9028[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Nov 28 01:23:16 2016 -0800

    general updates

M	cadpage-private

[33mcommit e80ac5fac1d5993738b2086fbcf661cd332c545a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 27 16:19:57 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASpokaneCountyParser.java
M	cadpage-private

[33mcommit a5a711ee1feb9596086a953882e94037c100da10[m
Merge: 99f764c 37d6326
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Nov 27 04:18:35 2016 -0800

    update user list

[33mcommit 37d6326353d2a9e19dc513557f1ef67f2e19723d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 26 21:24:29 2016 -0800

    $1

M	cadpage-private

[33mcommit 39b0615596cb2e46376f1a7543067d937764d1b3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 26 21:21:18 2016 -0800

    $1

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyParser.java
M	cadpage-private

[33mcommit 560f098e3c3e6c1b6bbb9227b18e25d3691de240[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 26 09:09:06 2016 -0800

    $1

M	build.gradle
M	cadpage

[33mcommit a65c8d97bec304425007e712cc112ebf84558816[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 25 11:24:40 2016 -0800

    $1

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWayneCountyAParser.java
M	cadpage-private

[33mcommit 109063b9233aaa4317e4a5bade1014c8161a8a81[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 24 09:57:38 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMuskegonCountyParser.java
M	cadpage-private

[33mcommit 22b3226448119e131ea127464bd410e0cf996f11[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 24 04:36:37 2016 -0800

    $1

M	cadpage-private

[33mcommit 4071172a5140c54dec360bfdf006f8d6cc75cb22[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 23 18:12:46 2016 -0800

    $1

M	cadpage-private

[33mcommit 7d5a246787d9184bf5f1b6eeb628bbcc6b39660b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 23 17:43:38 2016 -0800

    $1

M	cadpage-private

[33mcommit 70395997d9bd4d114622fb7aeac368450ec5e88d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 23 17:19:12 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBlountCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
M	cadpage-private

[33mcommit efcb4aa923941509a7b0cf99581b1afa1b0450c9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 23 13:40:14 2016 -0800

    $1

M	cadpage-private

[33mcommit 99f764ce4982314133e147b192e966ba5526ccea[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Nov 23 03:38:03 2016 -0800

    general updates.

M	cadpage-private

[33mcommit b35d6656ba5016dd1dbb92fdb0dc0b1c320200a9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 22 19:05:39 2016 -0800

    $1

M	cadpage-private

[33mcommit b72272935ed5288f8efa3c05897974c5ef5b6734[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 22 19:00:49 2016 -0800

    $1

M	cadpage-private

[33mcommit bc46ae95a4d1f609eb396d886e6255a93aab45f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 22 18:49:17 2016 -0800

    $1

M	cadpage-private

[33mcommit 4778d4280481896cded38557dc08cce92d826ccc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 22 18:24:49 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-private

[33mcommit dc2b00a1ec81319002799d586a0b0862e3effe8e[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Nov 22 03:25:52 2016 -0800

    general updates

M	cadpage-private

[33mcommit 512ba2b3c71406addb49977f213ad507a4a3c7f3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 21:31:47 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASouthLakeTahoeParser.java
M	cadpage-private

[33mcommit dd52290052d30b66b14d127433e9c4f50401dad5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 21:20:18 2016 -0800

    test

M	scripts/gcommit

[33mcommit 39c87e88ca0e8c0f77688d4e99463f50cd2fdcf5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 21:16:22 2016 -0800

    $1

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCABC/ZCABCNanaimoParser.java
M	cadpage-private

[33mcommit 7fea6a41803df9f6318404dc12de43590dde534b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 15:06:12 2016 -0800

    $1

M	scripts/gsync

[33mcommit c819c21d07bda4ac3d84eed49db7af3cc8c42206[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 15:05:15 2016 -0800

    $1

M	scripts/gpush
M	scripts/gsync

[33mcommit 3b6b3386435bea5a01c59c8530f350676f44f0f2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 15:02:34 2016 -0800

    $1

A	scripts/gsync

[33mcommit 230b16f65fde35d784d81e0517b31d8744b0a871[m
Merge: 062a0e9 ce1340b
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 11:50:31 2016 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers
    
    Conflicts:
    	cadpage-private

[33mcommit 062a0e9e39ecb676900110edec61c15730e1b246[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 11:49:06 2016 -0800

    Release v1.9.11-03

M	build.gradle
M	cadpage

[33mcommit ce1340b67b82322eec5e7323dd7bacabc904cd26[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Nov 21 09:57:27 2016 -0800

    general updates

M	cadpage-private

[33mcommit 489da8c438a56f668551c714c776bb5ceffa91a8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 21 09:55:32 2016 -0800

    New Location: Madison County, NE

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NE/NEMadisonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit f9e35387935e00d2d6f01437e3fa40da65a718c9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 20 15:52:59 2016 -0800

    Parsing problem with Carter County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyDParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCarterCountyParser.java
M	cadpage-private

[33mcommit d9c91e113e78e8ad611569d3412f21ea571e2589[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 20 15:00:57 2016 -0800

    Fixed mapping problem with Montgomery County, VA (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMontgomeryCountyBParser.java
M	cadpage-private

[33mcommit abcdfcf13bcb9e38cee8698597f5981a9bded512[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 20 08:33:28 2016 -0800

    v1.9.11-02

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit b062174df8482d8bf120071ee5454cc36faa80dd[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sun Nov 20 03:02:40 2016 -0800

    general updates

M	cadpage-private

[33mcommit fd1a755ae26ad769c2ad9ad1ef987a639841921a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 19:30:25 2016 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit 871f3e6d2fee146ab77d14c305ceddba387a3165[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 18:26:07 2016 -0800

    Fixed parsing probelm with Schuylkill County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASchuylkillCountyParser.java
M	cadpage-private

[33mcommit 774bd12149ed708ab8a9a0d959f6f1340fc5b6ec[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Nov 19 18:09:42 2016 -0800

    general updates

M	cadpage-private

[33mcommit c8e312ff0a693fca06532e6a8a8f19cbc595ad0b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 17:28:16 2016 -0800

    New Location Cayuga County, NY (B)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyParser.java
M	cadpage-private

[33mcommit fd98b4e34b1f777c71fc30a3c0212aeb47b37cb4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 16:39:15 2016 -0800

    Fixed parsing problem with Marion County, SC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMarionCountyParser.java
M	cadpage-private

[33mcommit c94b6b8f1bb7c6895b1fad2e20aba43ec42f16f3[m
Merge: 19e5a47 3d6c952
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 11:00:11 2016 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 3d6c9528b0cb348cfe30e359317060adfe1768a2[m
Merge: 98e154a 5c1f7d7
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 10:57:47 2016 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers
    
    Conflicts:
    	cadpage-private

[33mcommit 98e154a2be36bb64b01a13658451cfa1f78db162[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 10:55:31 2016 -0800

    Update msg doc

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCameronParser.java
M	cadpage-private

[33mcommit 19e5a47ef03a0d90d3fe0282685bf557ceae66d1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 10:50:51 2016 -0800

    Update msg doc

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyEParser.java
M	cadpage-private

[33mcommit 5c1f7d70bf956e5e9a863beca72abc8aa3bf3c89[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 19 09:11:37 2016 -0800

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit e7becc11dfc37cdae6cea4ffa2218e72a6d230bc[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Nov 19 03:36:24 2016 -0800

    general updates

M	cadpage-private

[33mcommit 6e08244baca9fb513c58b0248c06e9577d54a40b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 18 16:21:26 2016 -0800

    update gpush script

M	scripts/gpush

[33mcommit 91c7898d03288364b7c4129229e58f541750fc6a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 18 11:19:38 2016 -0800

    Release v1.9.11-01

M	build.gradle
M	cadpage

[33mcommit 05b9b17b8ff3a85a5cd1d644f11d78bd537da248[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Nov 18 02:39:59 2016 -0800

    general updates

M	cadpage-private

[33mcommit f0873c88e92326b6fb61eedaa11f1cfb89e065a3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 17 11:23:44 2016 -0800

    Fixed parsing problem with Collin County, TX (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCollinCountyAParser.java
M	cadpage-private

[33mcommit e704db31744db4c501d14c32376175d1e4454904[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 17 09:28:04 2016 -0800

    Fixed test problems

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStCharlesCountyParser.java
M	cadpage-private

[33mcommit ce5e7e9b4a234e80d214dfb310385dbc7cde50ff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 16 20:27:31 2016 -0800

    sync

M	cadpage

[33mcommit 7913362d0328bc09ceed884d3db303ec5646471e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 16 20:25:42 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit e4957ef8a13973d4f13f5e960825c8159e71e409[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 16 19:49:46 2016 -0800

    Update sender filter for Clay COunty, MO

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOClayCountyParser.java
M	cadpage-private

[33mcommit 32716d2673b29f55e50435e48a6aea7a4d69c340[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 16 19:44:43 2016 -0800

    Fixed parsing problem with St Charles County, MO

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStCharlesCountyParser.java
M	cadpage-private

[33mcommit 7345daf45d8788f59a60e18d880ac1b92d59cb26[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 16 17:10:49 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit b43a9d93a60b2b61badb305a81bdbc0d5cd3902b[m
Author: Jean Goul <jean@cadpage.org>
Date:   Wed Nov 16 00:53:59 2016 -0800

    general updates

M	cadpage-private

[33mcommit 79f048764bea4d41cc779d95dcdf8159a7513714[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 15 21:55:54 2016 -0800

    Update DinwiddieCounty, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADinwiddieCountyParser.java

[33mcommit 1653a8902b478a7008609f4ee60e81273b1c2078[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 15 21:46:28 2016 -0800

    Fixed parsing problem with Ocean County, NJ (B&C)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyCParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA19Parser.java
M	cadpage-private

[33mcommit da9f6a97dfafba25bfc13201921bb239a3830e7b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 15 18:54:37 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit da1bb638d9cf5670fbf52c1dab21f0f7b7cd087c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 15 17:40:05 2016 -0800

    Fixed parsing problem with Dinwiddie County, VA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADinwiddieCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAOrangeCountyParser.java
M	cadpage-private

[33mcommit 2869496f23f9690fb11715543b6121c9b4fb4c44[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 15 16:33:56 2016 -0800

    Update  msg doc

M	cadpage-private

[33mcommit a58f7b8b513e1a596b174a877895d205d3630e23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 15 16:27:15 2016 -0800

    Fixed parsing issue with Orange County, VA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAOrangeCountyParser.java
M	cadpage-private

[33mcommit 01d1d2670b4215ee35579df3e3a802411e1f0a45[m
Merge: c9c346e 22b058b
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 15 16:26:38 2016 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers
    
    Conflicts:
    	cadpage-private

[33mcommit 22b058b0031dca266e444ec171393b9610a0fb6c[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Nov 14 03:28:02 2016 -0800

    general updates

M	cadpage-private

[33mcommit c9c346edc9533392edbb577e8a9f942cb2f2d283[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 13 11:33:39 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 7797c22d6ceaf22b71a8e21c5af3ada59481bd82[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 13 11:13:38 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit 9625a349c969423331900364884b176a74d7f8ff[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 12 15:48:51 2016 -0800

    Fixed parsing problem with NCDavieCounty

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavieCountyParser.java
M	cadpage-private

[33mcommit 4a3610a61d4888c332aeeb487952c0fa2050e308[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Nov 12 01:43:00 2016 -0800

    general updates

M	cadpage-private

[33mcommit 474e0ac0f104797d90e3d328ce146c7cc2411718[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Nov 11 01:59:38 2016 -0800

    general updates

M	cadpage-private

[33mcommit 958a855ae29fbe7e9d3c483b72de3ea7d06ad001[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 10 19:16:11 2016 -0800

    Fixed parsing problem with Rowan County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRowanCountyParser.java
M	cadpage-private

[33mcommit abcb63df7636b10370d1b990de192c2608c31d4b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 10 17:53:35 2016 -0800

    Fixed parsing problem with Montecito, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontecitoParser.java
M	cadpage-private

[33mcommit 15a6706be9298bcea3642d9da5e20f2d26df71f2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 10 17:10:59 2016 -0800

    Parsing problem with Chatham-Kent, Ontario

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONChathamKentParser.java
M	cadpage-private

[33mcommit e968e489e5e31db92786ddaa673cab4db9b60cb8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 10 09:07:42 2016 -0800

    Update genome.log

M	cadpage-private

[33mcommit 4fd043c4087d74078de3351a3aa29257466492aa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 9 20:39:27 2016 -0800

    Fixed parsign problem with Hartford COunty, CT (Farmington County)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTHartfordCountyFarmingtonParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
M	cadpage-private

[33mcommit 912277eb50f92e8b8aa2cfa208d7ef6f7457007e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 9 19:55:21 2016 -0800

    Checking in MNRedwoodCounty

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRedwoodCountyParser.java
M	cadpage-private

[33mcommit 053a8f891b561f9fed1b0fd654e9f8aaacc6b283[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 9 15:03:55 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 0b8948364c852dea00daea27b204afb09f42f524[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 9 14:52:44 2016 -0800

    update genome.log

M	cadpage-private

[33mcommit 180c7b2a2789ab35e81effb113ed9c288105c082[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 9 01:52:53 2016 -0800

    Fixed parsing problem with Baltimore County, MD (B)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDBaltimoreCountyBParser.java
M	cadpage-private

[33mcommit adb950bd13d2fc18c6c57d7a882d71d7da9ee6b5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 9 01:22:07 2016 -0800

    ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGalvestonCountyParser.java
M	cadpage-private

[33mcommit 94ad80dc4591569ff094dca98f4c0e0e3dc67a65[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 8 21:57:34 2016 -0800

    Fixed parsing problem with Galveston County, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGalvestonCountyParser.java
M	cadpage-private

[33mcommit f54b9eb6ae03f28a1efdf07994b6401a79460fee[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 8 17:49:02 2016 -0800

    Update msg doc

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODouglasCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCAndersonCountyCParser.java
M	cadpage-private

[33mcommit 107169e2b4e07346fe0871d38b0a6aed3bb5445d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 8 17:09:00 2016 -0800

    Fixed parsing problem with Ontario County, PA (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOntarioCountyAParser.java
M	cadpage-private

[33mcommit 81f46794f7bc0e1da4ce2f90cd18c69cf55eb258[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 8 16:22:55 2016 -0800

    Fixed parsign problem with Fayette County, PA

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBParser.java
M	cadpage-private

[33mcommit 6598f25f3dfc3f30b51e0d00e355b90e226dc139[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 8 11:09:35 2016 -0800

    Fixed parsing probelm with Cape Girardeau County, MO (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCapeGirardeauCountyBParser.java
M	cadpage-private

[33mcommit adaf1ebc1d0189f0a210817bcf5155099e0636c0[m
Author: Jean Goul <jean@cadpage.org>
Date:   Tue Nov 8 03:29:58 2016 -0800

    general updates

M	cadpage-private

[33mcommit 9e14596ac754e1cad45f02e62e0f4d8b770036f9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 7 18:17:38 2016 -0800

    Fixed sender filter for Jefferson County, TN

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNJeffersonCountyParser.java
M	cadpage-private

[33mcommit a8591b30fb9087ccb3fafb40170d94751a972c60[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Nov 7 08:54:50 2016 -0800

    release v1.9.10.33

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 78c376cfeeafc34526f1a0b9c8d2eb9c0393c4c6[m
Author: Jean Goul <jean@cadpage.org>
Date:   Mon Nov 7 03:26:08 2016 -0800

    general updates

M	cadpage-private

[33mcommit 1856b75c47da95edf27235dd0461499816753958[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 21:01:12 2016 -0800

    Update msg doc

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORJacksonCountyParser.java
M	cadpage-private

[33mcommit 10540a09d9afbc3520815e1acba44d6623fe503c[m
Merge: 4a76a86 217e02a
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 20:55:39 2016 -0800

    Merge branch 'master' of github.com:cadpage/cadpage-parsers
    
    Conflicts:
    	cadpage-private

[33mcommit 4a76a86cb55272952d2fda5b9a2d03afe60f2782[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 19:00:14 2016 -0800

    update msg doc

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
M	cadpage-private

[33mcommit 217e02abfcc42eb047c229e53f96d91469ed7a99[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 19:00:14 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 9a0fbd1c7eba0726c34ab718c7fdd0270eaf09dc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 18:51:45 2016 -0800

    Fixed parsing problem with Pierce County, WA

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyEParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyParser.java
M	cadpage-private

[33mcommit fca7c032677b5819cac0eda67cad288eb002b534[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 16:07:12 2016 -0800

    Fixed parsing problem with O'Fallon, IL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILOFallonParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA33Parser.java
M	cadpage-private

[33mcommit dc68471fc73345ab5ebfec67f99d6ab770c863f1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 15:38:01 2016 -0800

    Update msg doc

M	cadpage-private

[33mcommit 1db70cb41d0d10ad2ca14ab529379d952bae4ed2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 13:27:05 2016 -0800

    Ditto

M	cadpage-private

[33mcommit 6dabdadd68c12c44d075096e5ded8d18900d65d0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Nov 6 13:26:09 2016 -0800

    Fixed parsing problem with Pennington COunty, SD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMcDowellCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDPenningtonCountyParser.java
M	cadpage-private

[33mcommit 89807961977d5a12f4854f1e03898f21654e65dc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 5 19:04:23 2016 -0700

    Fixed parsing problem with La Porte, TX

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLaPorteParser.java
M	cadpage-private

[33mcommit bc95aa4d0052ca34d8de424bfe1b6add511213d1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 5 15:34:39 2016 -0700

    Update msg doc

M	cadpage-private

[33mcommit f9bd2c93ac0d84b292d622efc28f6ccb02ff5e97[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 5 15:27:46 2016 -0700

    Fixed mapping problem with Lehigh COunty, PA (A)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALehighCountyAParser.java
M	cadpage-private

[33mcommit 758b19ffd376f88a344c0bb3d333c3b3226cd83d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Nov 5 14:51:30 2016 -0700

    Fixed parsing problem with DENewCastleCounty, DE (B & D)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyDParser.java
M	cadpage-private
M	docs/Getting_Started.txt

[33mcommit 7697099b3ab1b4f555769778032278d16492511a[m
Merge: d7d98f7 43152ea
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Nov 5 09:17:22 2016 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit d7d98f787b37e257b58b40abbf22df023e0f6f24[m
Author: Jean Goul <jean@cadpage.org>
Date:   Sat Nov 5 09:07:46 2016 -0700

    update user list

M	cadpage-private

[33mcommit 43152ea873cb2f5edc72b81aca8db6959b2fe455[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 21:41:00 2016 -0700

    update genome.log

M	cadpage-private

[33mcommit df8c695543d2729791383789990afbd68d54df2d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 21:22:02 2016 -0700

    Update msg doc

M	cadpage-private

[33mcommit cb1e48837f5b018e4a05293b062168703f4a4067[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 20:50:03 2016 -0700

    Fixed parsing problem with San Joaquin County, CA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanJoaquinCountyParser.java
M	cadpage-private

[33mcommit 39f7e4056b11b1e9c2001554ee31aefc0d65dd99[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 14:24:44 2016 -0700

    Update msg doc

M	cadpage-private

[33mcommit 819258586d6d100c1b70f7cd9f8859dd5b5eace7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 12:11:55 2016 -0700

    v1.9.10.32

M	build.gradle
M	cadpage

[33mcommit f6fab7c3c73088a155c6b76aff4f902a28fbf6e1[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 11:49:12 2016 -0700

    Update genome.log

M	cadpage-private

[33mcommit 7c1e4019c4f5f1a4d82e007a94694b7aaf5ffe6f[m
Merge: 5f4f2de 671a7b8
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 11:47:04 2016 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers
    
    Conflicts:
    	cadpage-private

[33mcommit 5f4f2de14362be8f0dc5600a57ff8ce258bc50b4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Nov 4 11:34:52 2016 -0700

    Fixed problem with Pennington County, SD

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDPenningtonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyBParser.java
M	cadpage-private
M	docs/Getting_Started.txt

[33mcommit 671a7b85708efbd081a3ec9fd87eac7ac7d17bde[m
Author: Jean Goul <jean@cadpage.org>
Date:   Fri Nov 4 02:07:12 2016 -0700

    general updates

M	cadpage-private

[33mcommit bc6760c5f991a2cec5e97c779e31029a39a116f4[m
Merge: 2ea3b8d ae2b7e1
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 3 21:20:25 2016 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 2ea3b8d35fa31552137d2de5fbec93cb9dd4fca0[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Nov 3 21:19:36 2016 -0700

    Update docs

M	docs/Getting_Started.txt

[33mcommit ae2b7e14e0dd1efc4a961a661985d0d15c92eccd[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 2 22:29:25 2016 -0700

    Update Msg doc

M	cadpage
M	cadpage-private

[33mcommit 64ea6f591bdda85d8a46d5bd5e7908dc31b51a23[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 2 13:20:44 2016 -0700

    Release 1.9.10.31

M	build.gradle
M	cadpage

[33mcommit 98c28c0decafed8290951f347c46f72597e219df[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 2 12:50:29 2016 -0700

    Updated A911 parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCarteretCountyParser.java
M	cadpage-private

[33mcommit c7ee20325c26506d301c57e110da7219871b1bc9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Nov 2 11:42:37 2016 -0700

    update user list and misc calls

M	cadpage-private

[33mcommit eda792ab1acde604b71d0db1f71c893f1fc93342[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Nov 1 15:35:30 2016 -0700

    Fixed parsing problem with Mesa County, CO

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMesaCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMesaCountyParser.java
M	cadpage-private
M	docs/support.txt

[33mcommit bbd49020ed4ddb1a40067db5caf3f30f29f8bf05[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 31 16:50:06 2016 -0700

    Pulled in last support documents from Eclipse project

M	cadpage-private
A	docs/A911Interface.txt
A	docs/Active911.txt
A	docs/CadpageParser.txt
A	docs/CadpageService.txt
A	docs/Cadpage_Publish.txt
A	docs/DonateMenu.txt
A	docs/GCMProtocol.txt
A	docs/Getting_Started.txt
A	docs/INDEX.txt
A	docs/Introduction.txt
A	docs/LocationParsers.txt
A	docs/Projects.txt
A	docs/SAParser.log
A	docs/support.txt

[33mcommit d264b280ed4e1ad09c70889f3e5a7658a115647a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 31 16:07:39 2016 -0700

    Release v1.9.10.30

M	build.gradle
M	cadpage
M	cadpage-private

[33mcommit 75b0a4e11e321d1d086b08fb84c0cc01809af280[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 31 16:06:11 2016 -0700

    Release v1.9.10.30

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALAutaugaCountyParser.java

[33mcommit 639579c7e757c26f59b12aad9c4221ba48c709a6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 31 15:07:06 2016 -0700

    added Autauga County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
M	cadpage-private

[33mcommit 62085e21453eda76b345147c56dc2fe1ec147617[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 31 11:36:13 2016 -0700

    Fixed parsing problem with Northampton County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthamptonCountyParser.java
M	cadpage-private

[33mcommit 7fe7a1e1c08063c3196870221e296a5b5affd0bc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 31 09:03:32 2016 -0700

    Resync cadpage-private

M	cadpage-private

[33mcommit f72a0c700e71958371cfaca10d8e2aa006d15b8e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 29 14:00:37 2016 -0700

    Release v1.9.10.29

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyCencommParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthumberlandCountyBParser.java
M	cadpage-private

[33mcommit 2a71e3296985759f2b989859a0a375d00424ed1a[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 29 10:12:10 2016 -0700

    Fixed parsing problem with Lincoln County, NC

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLincolnCountyParser.java
M	cadpage-private

[33mcommit f5d34ca7d95c56b312bdaba4fa6006005ab21d8c[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 20:26:08 2016 -0700

    Fixed parsing problem with Mesa County, CO

A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMesaCountyBParser.java
M	cadpage-private

[33mcommit 159f674100d9e2eba941e0acf58073d63fe6f933[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 14:25:39 2016 -0700

    Fixed parsing problem with Stark County, OH (Cencomm)

M	.classpath
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyCencommParser.java
M	cadpage-private

[33mcommit ac3cef6d054d6119b5cb26975f8841731e81ebfc[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 12:16:46 2016 -0700

    Update genome.log

M	cadpage-private

[33mcommit 7da8365d14d03bb15f287c3f9fc2a399b4fe93e2[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 11:55:59 2016 -0700

    Update msg doc

M	cadpage-private

[33mcommit da43142c733ac0e8ddaf52241cac130a7e7c891b[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 10:47:18 2016 -0700

    Minor update for Carroll County, MD (A)

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
M	cadpage-private

[33mcommit 5d0d8733a0d1412383d520d638371ca7747cfa5e[m
Merge: a7c0414 340afc4
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 10:30:45 2016 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit a7c0414422c3d3ccbfd16cb85301c1000308dbe8[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 08:01:04 2016 -0700

    Fixed parsing problem with Shelby County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALShelbyCountyParser.java
M	cadpage-private

[33mcommit 340afc4836316eb0080db1339747243808c18c86[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 28 08:01:04 2016 -0700

    Fixed parsing problem with Shelby County, AL

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALShelbyCountyParser.java
M	cadpage-private

[33mcommit 06a539949ce714e37967a652eb4c4c46c0ad50aa[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 27 20:33:36 2016 -0700

    Update A911 Parser table

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCamdenCountyParser.java
M	cadpage-private

[33mcommit df5d55de528b65f345485ae516fa0a23fa66402d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 27 10:29:26 2016 -0700

    Fixed parsing problem with Morrow County, OH

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMarionCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMorrowCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMorrowCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMorrowCountyParser.java
M	cadpage-private

[33mcommit 5bc7f04a5412eb2624c155bdb305be8ce77e6bd6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 27 08:33:31 2016 -0700

    New location Cook COunty, IL (I)

M	cadpage
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyIParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyAParser.java
M	cadpage-private

[33mcommit f801bf29acb20c4e12fe964d4888e804dd662901[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 26 22:04:46 2016 -0700

    update genome.log

M	cadpage-private

[33mcommit 061d730495697df347a4a9ef6b0a293bf50bb3b6[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 26 21:51:15 2016 -0700

    Fixed parsing problem with Carteret County, NC

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCarteretCountyParser.java
M	cadpage-private

[33mcommit c3fc534fba319fdeb5e241b9d65fd8f0fb46f884[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 26 20:42:05 2016 -0700

    Ditto

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyParser.java
M	cadpage-private

[33mcommit 448de270f1cd2708781f23c0896b025f85135876[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 26 20:26:41 2016 -0700

    Fixed parsing problem with QUeen Annes County, MD

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyParser.java
M	cadpage-private

[33mcommit f47c3438c49f9ee747f90e7a891ad17578e237bb[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Wed Oct 26 14:37:42 2016 -0700

    Fixed parsing problem with Davis County, UT (E)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTDavisCountyEParser.java
M	cadpage-private

[33mcommit 32d5cdd20cf192d48d4fdcd9b219b8b553272464[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Tue Oct 25 16:47:46 2016 -0700

    Fixed parsing problems with Berks County, PA
    New Parser Northumberland County, PA (B)

A	.settings/org.eclipse.jdt.core.prefs
A	.settings/org.eclipse.jdt.ui.prefs
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABerksCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthumberlandCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthumberlandCountyParser.java
M	cadpage-private

[33mcommit 5b9aaba1ae095c0588500ec6a7bc5b78bf6eafbb[m
Merge: 1c439ae cdd013e
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 24 12:40:06 2016 -0700

    Merge branch 'master' of github.com:cadpage/cadpage-parsers

[33mcommit 1c439aefdabb322a94e4fc73173f8d7460109d65[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 24 12:39:38 2016 -0700

    update gradle build

M	build.gradle

[33mcommit cdd013ef1e7bc4a9ced33a1a770886b781d9b7d5[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 24 07:42:09 2016 -0700

    Parsing problems with Northumberland County, PA

M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABerksCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthumberlandCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthumberlandCountyParser.java
M	cadpage-private

[33mcommit 0aeafa00234d81cf1e224de7a1301caa881738d5[m
Author: Jamie Harper <snailtk@gmail.com>
Date:   Sun Oct 23 16:03:11 2016 -0700

    Setup new Eclipse project

A	.classpath
M	.gitignore
A	.project
M	cadpage-private

[33mcommit 2c400766f60a52fc629115689d2a963660cdb7ba[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 23 13:21:02 2016 -0700

    Update A911 parser table

M	cadpage
M	cadpage-private

[33mcommit fa573eeba2d346a76e88d1e53030afc540fd349f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 23 12:58:20 2016 -0700

    Parsing problem with Shelby County, OH

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHShelbyCountyParser.java
M	cadpage-private

[33mcommit 3e351d14d7b78a0a1cd5c880f4ffb01b64ce5f82[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 23 10:16:24 2016 -0700

    Fixed parsing problem with Twin Falls County, ID (B)

M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDGoodingCountyBParser.java
M	cadpage-private

[33mcommit d543fdc2dc4faec5e1054fbd8210a40206f2e46e[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 22 18:28:00 2016 -0700

    More synchronizaing

M	cadpage-private
M	scripts/gcommit

[33mcommit f280d1875006dc269126ca9ac9efe5a625b26c27[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sat Oct 22 16:57:32 2016 -0700

    More synchronization stuff

M	cadpage
M	cadpage-private
A	scripts/gall

[33mcommit 7f35061dfc8a27e17545220bf017e7d4b1276023[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 21 22:05:19 2016 -0700

    More synchronization  script improvements

M	build.gradle
M	scripts/gcommit

[33mcommit a588a4a1e178f660113f304743d590ffe607c6c4[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 21 21:16:01 2016 -0700

    Hopefully final sync fro mEclipse project

M	cadpage
M	cadpage-private

[33mcommit ae5f31d21e9fdddc0fa7b9974fff7296e198d693[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 21 20:30:02 2016 -0700

    Final (hopefully) sync with Eclipse project

M	build.gradle
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNewYorkCityParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAElkCountyParser.java

[33mcommit e77b75d609b41efaac91e906a8abdb0611ac745d[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 21 19:47:05 2016 -0700

    More adjustements to synchronization scripts

M	build.gradle
M	scripts/gcommit
M	scripts/gpull

[33mcommit 992ca8c1eb3b19378848d1968aaf84afb2a296af[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 21 14:48:07 2016 -0700

    Ditto

M	scripts/gpull

[33mcommit 95703cb0e645d3e5e6dc994afa90647d7101bc73[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 21 14:36:34 2016 -0700

    More work on syncrhonization scripts

A	scripts/gpull

[33mcommit c603dfbc3e72fb9378626b1d40b765bca74f9a0f[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Fri Oct 21 14:11:22 2016 -0700

    Update synchronization scripts

M	scripts/gcommit
A	scripts/gpush
A	scripts/gssh-add

[33mcommit a1971847f3197a3420eeae594f5bcb6ff096a7a3[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Thu Oct 20 09:07:36 2016 -0700

    Sync from Exclipse project

M	build.gradle
M	cadpage
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAJacksonCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyBParser.java
M	cadpage-private
A	scripts/common
A	scripts/gcommit
A	scripts/gstatus

[33mcommit 57f7ba7226e89d2e268c6458ffee44d33d30fce7[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Mon Oct 17 18:00:47 2016 -0700

    Sync with Eclipse project

M	build.gradle
M	cadpage
M	cadpage-parsers/build.gradle
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMontcalmCountyParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWilsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyAParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyBParser.java
M	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyParser.java
M	cadpage-private

[33mcommit 3bdbfd34853ef5b79370360042b5bfb9f5610d02[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 16 20:19:31 2016 -0700

    Initial commit

A	.gitmodules
A	cadpage
A	cadpage-private

[33mcommit 2a90db8403cd8825426b6534c858bcf67ad181e9[m
Author: Ken Corbin <ken@cadpage.org>
Date:   Sun Oct 16 19:44:59 2016 -0700

    Initial commit

A	.gitignore
A	build.gradle
A	cadpage-parsers/.gitignore
A	cadpage-parsers/build.gradle
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AK/AKFairbanksParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AK/AKJuneauParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AK/AKKenaiPeninsulaBoroughParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AK/AKMatanuskaSusitnaBoroughParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBaldwinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALBlountCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCalhounCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCalhounCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCalhounCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALChambersCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCherokeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALChiltonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCleburneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALCoffeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALColbertCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALDallasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALDothanAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALDothanBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALDothanParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALEtowahCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALGenevaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALGenevaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALGenevaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALHenryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALHooverParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLauderdaleCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLauderdaleCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLauderdaleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALLimestoneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMarshallCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMarshallCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMarshallCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMobileCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALMorganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALOzarkParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALPelhamParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALRussellCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALRussellCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALRussellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALShelbyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALStClairCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALTalladegaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AL/ALWalkerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARBentonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARBentonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARBentonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARBentonCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARBentonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARGarlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARGrantCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARHotSpringCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARPopeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARPulaskiCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARPulaskiCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARPulaskiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARWashingtonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARWashingtonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AR/ARWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZMaricopaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZNavajoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZNavajoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZNavajoCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZNavajoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZYavapaiCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZYavapaiCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZYavapaiCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZYavapaiCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZYavapaiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AZ/AZYumaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Active911Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/AliasedMsgParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Base64.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAAmadorCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAButteCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAButteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CACalaverasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CACathedralCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAContraCostaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAContraCostaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAContraCostaCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAContraCostaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAElDoradoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAFortIrwinParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAFresnoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAGroverBeachParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAHanfordParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAHaywardParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAHumboldtCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAKernCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALosAngelesCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALosAngelesCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CALosAngelesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMaderaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMarinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMendocinoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMendocinoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMendocinoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontecitoParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAMontereyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CANapaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CANapaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CANevadaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAOrangeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAOrovilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPasoRoblesParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAPlacerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CARiversideCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASacramentoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanBernardinoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanDiegoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanJoaquinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanLuisObispoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASanMateoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaClaraCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaClaraCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaClaraCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaCruzCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaCruzCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASantaCruzCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAShastaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAShastaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAShastaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASiskiyouCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASolanoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASonomaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASouthLakeTahoeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAStanislausCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAStocktonAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAStocktonBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAStocktonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASuisunCityAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASuisunCityBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CASuisunCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATulareCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CATuolumneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAVenturaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYoloCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYoloCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CA/CAYoloCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COAdamsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COArapahoeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COArchuletaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBentCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBoulderCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COBroomfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COChaffeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODeltaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODouglasCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODouglasCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CODouglasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COEagleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COElPasoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COElPasoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COElPasoCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COElPasoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COElbertCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COFremontCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COGarfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COGoldenParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COGrandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COGunnisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COHinsdaleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COKitCarsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLaPlataCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COLarimerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMemorialStarParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMesaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMontezumaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMontroseCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COMorganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/CONorthglennEMSParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COParkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COPitkinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COPuebloCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COSanJuanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COTellerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COThorntonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CO/COWeldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTBethelParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTBloomfieldParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTBrookfieldParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTCheshireParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTColchesterEmergCommParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTEastLymeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTFairfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTGrotonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTHartfordCountyAvonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTHartfordCountyEastGranbyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTHartfordCountyFarmingtonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTHartfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTLitchfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTMadisonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTMiddlesexCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTMiddletownParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewHavenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewLondonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewLondonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewLondonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNewMilfordParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTNorthwestPublicSafetyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTOldSaybrookParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTSimsburyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTStamfordParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTollandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTollandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTollandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTTrumbullParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTWaterfordTownParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTWindhamCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTWindhamCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTWindhamCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CT/CTWindhamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Cadpage2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Cadpage3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CadpageParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CadpageParserBase.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CodeSet.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/CodeTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DC/DCProteanhubParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEDelmarParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEKentCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DENewCastleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DESussexCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DESussexCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DESussexCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEWilmingtonAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEWilmingtonBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/DE/DEWilmingtonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLBayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLBrowardCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCalhounCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCharlotteCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCharlotteCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCharlotteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCitrusCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCocoaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCollierCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCollierCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCollierCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCoralSpringsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLCrestviewParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLEscambiaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLGulfBreezeAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLGulfBreezeBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLGulfBreezeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLHendryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLHernandoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLLakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLLeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLLevyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLManateeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLMiamiDadeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLOkaloosaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLOrangeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLPalmBeachCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLPensacolaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLPutnamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLSarasotaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLSatelliteBeachParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLSeminoleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLSumterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FL/FLWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/FieldProgramParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GABarrowCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GABrantleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GABullochCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GABullochCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GABullochCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAButtsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GACamdenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GACamdenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GACamdenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GACandlerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GACarrollCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAChathamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GACherokeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAClaytonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GACoffeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADadeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADecaturCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADoolyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GADoughertyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEffinghamCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEffinghamCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEffinghamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAEvansCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAFanninCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAFloydCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAForsythCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAFranklinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAGordonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAHabershamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAHallCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAHenryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAHoustonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAJasperCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GALibertyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAMaconCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAMartinezParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAMcDuffieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAMeriwetherCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAOconeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAPauldingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAPickensCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAPikeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GARabunCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GASchleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GASmyrnaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GASumterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GATalbotCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GATaylorCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GATiftCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWalkerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWaltonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWebsterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWhiteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWhitfieldCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWhitfieldCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWhitfieldCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GA/GAWhitfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GroupBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GroupBestParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/GroupBlockParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/HtmlDecoder.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/HtmlProgramParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAAppanooseCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IABlackHawkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IACerroGordoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IADallasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IADesMoinesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IADickinsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAJasperCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAJohnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAMitchellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAMuscatineCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAPolkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAPottawattamieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAScottCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAStoryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWarrenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWarrenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWoodburyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IA/IAWorthCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDAdaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDBinghamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDBlaineCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDBonnerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDGoodingCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDGoodingCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDGoodingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDJeromeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDJeromeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDJeromeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDKootenaiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDLincolnCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDLincolnCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDShoshoneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDTetonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDTwinFallsCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDTwinFallsCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ID/IDTwinFallsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILAdamsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILBooneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChampaignUrbanaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILChristianCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILClintonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILColumbiaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILCookCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILDuPageCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILElginParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILHancockCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILIroquoisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKaneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILKankakeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILLakeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILLakeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILLakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMacoupinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMassacCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMcHenryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMedstarParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILOFallonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILPeoriaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILPeoriaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILPeoriaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILRandolphCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILRandolphCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILRandolphCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILRichlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILRockIslandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILSangamonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStClairCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStClairCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStClairCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILStarkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILTazewellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILWinnebagoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IL/ILWoodfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INAdamsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INBartholomewCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INBooneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INBooneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INBooneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INBrownCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INClayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INDaviessCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INDelawareCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INDouglasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INElkhartCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INFloydCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INGrantCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHamiltonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHancockCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHancockCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHancockCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHenryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INHowardCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INJasperCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INJohnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INKnoxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INKosciuskoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INLakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INLaporteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMadisonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMadisonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMadisonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMarshallCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INMiamiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INParkeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INPorterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INPoseyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INPulaskiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INRandolphCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INShelbyCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INShelbyCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INShelbyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStJosephCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INStarkeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INTiptonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INVanderburghCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INVermillionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INVigoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INVigoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INVigoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INWabashCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INWayneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INWayneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INWayneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/IN/INWhitleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSAndersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSAndoverParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSAtchisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSBartonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSBourbonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSBrownCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSButlerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSCowleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSEllisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSEudoraParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSHarveyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSJohnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSKingmanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSLabetteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSLeavenworthCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMcPhersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMiamiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSMortonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSNeoshoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSPottawatomieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSRenoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSRiceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSRileyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSRooksCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSSedgwickCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSWichitaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KS/KSWyandotteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYAllenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYAndersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBooneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBooneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBooneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBourbonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBowlingGreenParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBoydCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBrackenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYBullittCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCallowayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCampbellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarrollCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarrollCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarrollCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarrollCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYCarterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYChristianCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYClarkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYDaviessCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYErlangerDispatchParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYFloydCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYFranklinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYFultonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGallatinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGallatinCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGallatinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGrantCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGravesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYGreenupCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHancockCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHardinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHardinCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHardinCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHardinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHarrisonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHarrisonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHarrisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHartCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYHenryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYJohnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYKentonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLaRueCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLawrenceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLoganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLouisvilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYLyonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMarshallCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMcLeanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMeadeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMetcalfeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMonroeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMuhlenbergCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYMurrayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYOldhamCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYOldhamCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYOldhamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYOwenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPendletonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPendletonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPendletonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPikeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPowellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYPulaskiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYRobertsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYRockCastleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYScottCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYScottCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYScottCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYShelbyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYSimpsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYSpencerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYStatePoliceParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYToddCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYTriggCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYTrimbleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/KY/KYWoodfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAAcadianAmbulanceParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAAscensionParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAAvoyellesParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LABeauregardParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LACalcasieuParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAEastFelicianaParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAJeffersonParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LALafayetteParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LALafourcheParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LALivingstonParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAOuachitaParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStCharlesParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStJamesParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStTammanyParishAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStTammanyParishBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAStTammanyParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATangipahoaParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LATerrebonneParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAWebsterParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAWestBatonRougeParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/LA/LAWestFelicianaParishParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MA/MABarnstableCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MA/MADukesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MA/MANantucketCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MA/MANorfolkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MA/MAYarmouthParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAlleganyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyADSiCADParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyAnnapolisParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyEMSParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyFireParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyFireblitzParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDAnneArundelCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDBaltimoreCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDBaltimoreCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDBaltimoreCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDBaltimoreParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCalvertCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCambridgeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarolineCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCarrollCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCecilCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCecilCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCecilCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCecilCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCecilCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCecilCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCharlesCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCharlesCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCharlesCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDCharlesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDDorchesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDFrederickCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDGarrettCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDHarfordCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDHarfordCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDHarfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDHowardCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDKentCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDMontgomeryCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDMontgomeryCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDNorthEastParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDOceanCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyFireBizParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDPrinceGeorgesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDQueenAnnesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSaintMarysCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDSomersetCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDTalbotCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWicomicoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWicomicoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWicomicoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWorcesterCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWorcesterCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MD/MDWorcesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ME/MEAndroscogginCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ME/MECumberlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ME/MEKnoxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ME/MEOxfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ME/MEYorkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIAlconaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIAlleganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIAlpenaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIAntrimCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIArenacCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBarryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBayCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBayCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIBerrienCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICalhounCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICalhounCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICalhounCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICalhounCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICassCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICharlevoixCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MICheboyganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIChippewaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIClareCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIClintonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIEatonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIEmmetCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIGogebicCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIGrandTraverseCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIGratiotCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIHillsdaleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIInghamCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIInghamCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIInghamCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIInghamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIIoniaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIIoscoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIIronCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIIsabellaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIKalamazooCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIKentCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MILakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MILeelanauCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MILenaweeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MILenaweeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MILenaweeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MILivingstonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MILuceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMackinacCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMarquetteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMecostaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMidlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMobileMedicalResponseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMontcalmCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIMuskegonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOaklandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOsceolaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOttawaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOttawaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIOttawaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIRichmondParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIRoscommonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MISaginawCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIShiawasseeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIStClairCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MI/MIWashtenawCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNAnokaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNAnokaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNAnokaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNBeckerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNBeltramiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNBlueEarthCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNBrownCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNCarltonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNCarverCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNCassCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNCookCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNCottonwoodCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNCrowWingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDakotaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDodgeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNDouglasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNEdenPrairieParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNEdinaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNFaribaultCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNFreebornCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNGoodhueCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNHennepinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNHubbardCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNIsantiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNItascaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNKandiyohiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNKoochichingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNLakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNLakeOfTheWoodsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNLeSueurCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNLyonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMartinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMcLeodCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMinneapolisStPaulAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMinneapolisStPaulBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMinneapolisStPaulParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNMowerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNNicolletCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNNorthMemorialAmbulanceServiceParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNOlmstedCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNOtterTailCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNPenningtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNPineCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNPopeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRamseyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNRenvilleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNScottCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNSherburneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNSibleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNStLouisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNStLouisParkParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNStevensCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNWabashaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNWadenaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNWasecaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNWatonwanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNWinonaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MN/MNWrightCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOAdairCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOAndrewCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOAudrainCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBarryCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBarryCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBarryCountyGPSTable1.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBarryCountyGPSTable2.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBarryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBentonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBooneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBransonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBuchananCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBuchananCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOBuchananCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCallawayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCamdenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCameronParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCapeGirardeauCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCapeGirardeauCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCapeGirardeauCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCarrollCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCentraliaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOChristianCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOClayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOClintonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOColeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCrawfordCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCrawfordCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOCrawfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MODallasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOFarmingtonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOFestusAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOFestusBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOFestusParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOFranklinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOGasconadeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOGreeneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOGreeneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOGreeneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOHarrisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOIronCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJacksonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJasperCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJeffersonCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOJohnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOLawrenceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOLivingstonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMaconCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMcDonaldCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMcDonaldCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMcDonaldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMonettParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOMoniteauCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MONewtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MONodawayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOOsageCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPettisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPhelpsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPikeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPolkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPoplarBluffParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOPulaskiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MORayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOShelbyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSikestonAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSikestonBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSikestonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStCharlesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStFrancoisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStLouisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSteGenevieveCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStoddardCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStoneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStoneCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOStoneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOSullivanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOWashingtonCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MO/MOWebsterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSAcadianAmbulanceParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSBiloxiParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSCalhounCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSDeSotoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSHernandoParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSLafayetteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSLauderdaleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSNeshobaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSNewtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MS/MSOktibbehaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTCascadeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTFlatheadCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTGallatinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTLewisAndClarkCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTLewisAndClarkCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTLewisAndClarkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTMissoulaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTParkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTRavalliCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MT/MTStillwaterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ManageParsers.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Message.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MessageBuilder.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgInfo.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/MsgParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAlamanceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAlexanderCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAlleghanyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCAsheCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBeaufortCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBladenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBrunswickCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBuncombeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCBurkeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCabarrusCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCabarrusCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCabarrusCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCaldwellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCarteretCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCaswellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCatawbaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCChathamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCherokeeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCChowanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCClayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCClevelandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCClevelandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCClevelandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCColumbusCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCravenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCumberlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCCurrituckCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDareCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavidsonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavidsonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavidsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDavieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDuplinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCDurhamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCEdgecombeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCForsythCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCFranklinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGastonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGastonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGastonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGastonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGatesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGrahamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGranvilleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGreeneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCGuilfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHalifaxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHarnettCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHarnettCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHarnettCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHarnettCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHavelockParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHaywoodCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHendersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHertfordCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHertfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHokeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCHydeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCIredellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCJohnstonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCJonesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLenoirCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLenoirCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLenoirCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCLumbertonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMaconCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMartinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMartinCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMartinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMcDowellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMecklenburgCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMecklenburgCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMecklenburgCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMitchellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCMooreCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNashCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNewHanoverCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCNorthamptonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCOnslowCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCOrangeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCOrangeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCOrangeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPamlicoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPasquotankCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPenderCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPittCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCPolkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRandolphCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRobesonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRockinghamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRockyMountParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRowanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCRutherfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCSampsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCStanlyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCStokesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCSurryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCTransylvaniaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCUnionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCVanceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWakeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWakeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWataugaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWayneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWayneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWayneCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWayneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWilkesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCWilsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCYadkinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NC/NCYanceyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDCassCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ND/NDPierceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NE/NEGrandIslandParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHGraftonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHGraftonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHGraftonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHHanoverParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHHollisParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHKeeneMutualAidParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NH/NHMerrimackCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJAtlanticareEMSParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBergenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJBurlingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCENCOMParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCamdenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCamdenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCamdenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCapeMayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJCumberlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJEssexCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJGloucesterCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJGloucesterCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJGloucesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJHunterdonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJJCMCEMSJerseyCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMICOMAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMICOMBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMICOMParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMarlboroParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMedCenterParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMercerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMercerCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMercerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMiddlesexCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMiddlesexCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMiddlesexCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMonmouthCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMonmouthCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMonmouthCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJMorrisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJNeptuneParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJOceanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJPassaicCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSalemCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSalemCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSalemCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSomersetCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSomersetCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSomersetCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSomersetCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJSussexCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NJ/NJWayneTownshipParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NM/NMChavesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NM/NMDonaAnaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NM/NMDonaAnaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NM/NMDonaAnaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NM/NMLasCrucesParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NM/NMSanJuanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVClarkCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVClarkCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVClarkCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVClarkCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVClarkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVElkoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVElkoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVElkoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NV/NVHumboldtCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYAlbanyCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYAlbanyCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYAlbanyCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYAlbanyCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYAlbanyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYAlleganyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBethlehemParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBroomeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYBuffaloParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCattaraugusCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCayugaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYChautauquaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYChenangoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYClintonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYColumbiaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCortlandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCortlandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCortlandCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYCortlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYDelawareCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYDixHillsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYDuchessCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYErieCountyRedAlertParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYEssexCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYFranklinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYFultonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYGeneseeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYGreeneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYHerkimerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYHerkimerCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYHerkimerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYJeffersonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYJeffersonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLewisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLivingstonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLivingstonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLivingstonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYLivingstonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyGLASParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMonroeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMonroeCountyRuralMetroParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMonroeCountyWebsterParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyBethpageParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyElmontParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyFiretracker2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyFiretracker3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyFiretrackerParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyIParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyJParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyKParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyMParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyMassepequaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyNParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNassauCountyRedAlertParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNewYorkCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNiagaraCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNiagaraCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNiagaraCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYNightwatchSecurityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOneidaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyMetroWestParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOnondagaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOntarioCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOntarioCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOntarioCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOrangeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOrangeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOrangeCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOrangeCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOrangeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOrleansCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOswegoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOswegoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOswegoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYOtsegoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYPutnamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYRensselaerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYRocklandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYRocklandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYRocklandCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYRocklandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSomersParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYStLawrenceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSteubenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyAllParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyFiretrackerParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyIParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyJParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyKParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSuffolkCountyXBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYSullivanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYTiogaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYUlsterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWayneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWayneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWayneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWestchesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/NY/NYWyomingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAdamsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAllenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAllenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAllenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAshtabulaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAthensCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHAuglaizeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHBelmontCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHBrownCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHButlerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHButlerCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHButlerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCarrollCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCentervilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHChampaignCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClarkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClermontCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHClintonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHColumbianaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCrawfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHCuyahogaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHDarkeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHDelawareCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHEnglewoodParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHFairfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHFairfieldParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHFranklinParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHFultonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGalliaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGeaugaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGeaugaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGeaugaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGenevaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHGreeneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHamiltonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHamiltonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHamiltonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHancockCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHighlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHockingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHolmesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHHudsonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHKnoxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLawrenceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLickingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLimaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLoganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLorainCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLorainCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLorainCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLovelandParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHLucasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMadisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyCencommParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMahoningCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMarionCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMarionCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMedinaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMeigsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMercerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMiamiCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMonroeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMorrowCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMuskingumCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMuskingumCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHMuskingumCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHNECCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHOxfordParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPerryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPickawayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyCencommParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPortageCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHPrebleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHShakerHeightsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHShelbyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyCencommParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyRedcenter2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHStarkCountyRedcenterParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSugarCreekParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHSummitCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHTrentonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHTrumbullCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHUnionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWadsworthAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWadsworthBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWadsworthParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWarrenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWarrenCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWarrenCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWarrenCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWashingtonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWashingtonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWayneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWayneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWayneCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWayneCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OH/OHWayneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKBryanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKCarterCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKCarterCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKCarterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKCherokeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKClevelandCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKClevelandCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKClevelandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKGarfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKLoveCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKMayesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKMuskogeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKOklahomaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKPayneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKPontotocCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKPottawatomieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKSandSpringsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKSeminoleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTinkerAFBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKTulsaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OK/OKYukonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORBentonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClackamasCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClackamasCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClackamasCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClackamasCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClackamasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORClatsopCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORColumbiaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORCrookCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORDeschutesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORDouglasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORGilliamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORHoodRiverCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORJacksonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORJosephineCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORKlamathCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLakeOswegoParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLaneCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLaneCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLaneCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLaneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLincolnCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORLinnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMultnomahCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMultnomahCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMultnomahCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORMultnomahCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORPolkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORShermanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORTillamookCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORUmatillaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORWheelerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORYamhillCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORYamhillCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/OR/ORYamhillCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAdamsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAAlleghenyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAArmstrongCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAArmstrongCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAArmstrongCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABeaverCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABerksCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABethlehemParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABlairCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABlairCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABlairCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABradfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PABucksCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAButlerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACambriaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACentreCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD1Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD4Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyD5Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyIParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyJParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyLParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyMParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyNParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyOParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAChesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClearfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAClintonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAColumbiaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAColumbiaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAColumbiaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACrawfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PACumberlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADauphinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyHParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PADelawareCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAElkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAErieCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAErieCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAErieCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAErieCountyEmergyCareParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAErieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFayetteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFultonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAGranthamParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAGreeneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAJuniataCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyAmbulanceParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALackawannaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALancasterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALawrenceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALebanonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALehighCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALehighCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALehighCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALuzerneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PALycomingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMercerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMifflinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMonroeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMonroevilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyFParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyGParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthamptonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PANorthumberlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAPennStarParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAPerryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAPikeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAPikeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAPikeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASchuylkillCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASnyderCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASomersetCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PASusquehannaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PATiogaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAVenangoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAVenangoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAVenangoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWayneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWestmorelandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAWyomingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAYorkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ParserList.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/RI/RIProvidenceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/RI/RIWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/RI/RIWestWarwickParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ReverseCodeSet.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ReverseCodeTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SAPWrapper.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCAbbevilleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCAndersonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCAndersonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCAndersonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCAndersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCharlestonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCCherokeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCChesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCClarendonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCDarlingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCDorchesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCFairfieldCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCFlorenceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGeorgetownCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGeorgetownCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGeorgetownCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGreenvilleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCGreenwoodCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCJasperCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCKershawCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCLancasterCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCLancasterCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCLancasterCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCLancasterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCLeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCLexingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCMcCormickCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCNewberryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOconeeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOconeeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOconeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCOrangeburgCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCPickensCountyNoInfoParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCPickensCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCRichlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSpartanburgCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSumterCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSumterCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCSumterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SC/SCYorkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDMinnehahaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDPenningtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDUnionCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDUnionCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDUnionCountyBaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SD/SDUnionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SmartAddressParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SplitMsgOptions.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/SplitMsgOptionsCustom.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/StandardCodeTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNAndersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBedfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBlountCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBlountCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBlountCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNBradleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCampbellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCarterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNClayCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCockeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNCumberlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNDyerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNHamiltonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNHamiltonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNHamiltonCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNHamiltonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNHumphreysCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNJohnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNKingsportParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNKnoxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNLoudonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNMooreCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNMorganCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNMorganCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNMorganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNOakRidgeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNOliverSpringsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNOvertonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNRheaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNRoaneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSevierCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNSumnerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNUnionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWashingtonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWashingtonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWeakleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TN/TNWilliamsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAcadianAmbulanceParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAngletonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXAransasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBexarCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBrazoriaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBrazoriaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBrazoriaCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBrazoriaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXBrazosCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCarrolltonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCollinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCollinCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCollinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCookeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCrowleyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyCreekCommCenterParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXCyFairParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDallasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDalworthingtonGardensParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDecaturParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDentonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDentonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXDentonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXElCampoParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFlightVectorParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFlowerMoundParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFortBendCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFortBendCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFortBendCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXFortWorthParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGainesvilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGalvestonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGatesvilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXGreggCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyESD1AParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyESD1BParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyESD1Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyNWEMSParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHarrisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHaysCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHaysCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHaysCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHaysCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHelotesParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHoodCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHumbleAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHumbleBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHumbleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHuntCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHuntCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHuntCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXHutchinsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXJohnsonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXJohnsonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXJohnsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKaufmanCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKaufmanCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKaufmanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKellerParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKendallCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXKilgoreParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLaPorteParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLampasasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLeagueCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLeonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLeonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLeonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLewisvilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLibertyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLongviewParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLubbockCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLubbockCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLubbockCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXLufkinParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMansfieldParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXManvelParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMcLennanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMidlothianParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMissouriCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMontgomeryCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMontgomeryCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNacogdochesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNavarroCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNavasotaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXNuecesCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXOvertonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXParkerCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXParkerCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXParkerCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXParkerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXPortAransasParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRoanokeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRockwallCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRockwallCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRockwallCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRosenbergParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRoyseCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRuskCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRuskCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXRuskCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXSeabrookParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXSeguinParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXSmithCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXSomervellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXStaffordParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTarrantCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTarrantCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTarrantCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTarrantCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTexasCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXTravisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXVanAlstyneParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXVanZandtCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXVillageParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWalkerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWebsterParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWhartonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWilliamsonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TX/TXWylieParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/TestCodeSet.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTBoxElderCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTDavisCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTDavisCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTDavisCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTDavisCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTDavisCountyEParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTDavisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSaltLakeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTSummitCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTTooeleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTWeberCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTWeberCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/UT/UTWeberCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAccomackCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlbemarleCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlbemarleCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlbemarleCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlbemarleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAlexandriaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAmeliaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAppomattoxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAArlingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAAugustaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABedfordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABotetourtCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VABrunswickCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACampbellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACarolineCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACarolineCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACarolineCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACarrollCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAClarkeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAColonialHeightsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VACulpeperCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADanvilleParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADickensonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VADinwiddieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFairfaxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFallsChurchParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFauquierCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFluvannaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFluvannaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFluvannaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFranklinParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFrederickCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAFredericksburgParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGalaxParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGloucesterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGoochlandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAGreeneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHalifaxCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHanoverCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHanoverCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHanoverCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHenricoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHenryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAHopewellParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAIsleOfWightCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAJamesCityCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAKingGeorgeCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAKingGeorgeCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAKingGeorgeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALoudounCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALoudounCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALoudounCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALoudounCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALouisaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VALunenburgCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMecklenburgCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMontgomeryCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMontgomeryCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAMontgomeryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VANewKentCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VANewportNewsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VANorthamptonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAOrangeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPageCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPetersburgParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPittsylvaniaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPowhatanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPrinceEdwardCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPrinceGeorgeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAPrinceWilliamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARadfordParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARichmondParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCityParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARoanokeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARockbridgeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VARockinghamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAShenandoahCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASmythCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASouthamptonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASouthamptonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASouthamptonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASpotsylvaniaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAStaffordCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAStauntonParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASuffolkParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VASurryCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAUniversityOfVirginiaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWarrenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWashingtonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWaynesboroAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWaynesboroBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWaynesboroParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWinchesterParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWiseCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VA/VAWytheCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTAddisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTChittendenCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTChittendenCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTChittendenCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTGrandIsleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTHartfordParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTLamoilleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTWindhamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/VT/VTWindsorCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WABentonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAChelanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAClallamCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAClarkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WACowlitzCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAGrantCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAGraysHarborCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAIslandCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKitsapCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAKlickitatCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WALewisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAMasonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAOkanoganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPendOreilleCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAPierceCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASkamaniaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASnohomishCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WASpokaneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAStevensCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAThurstonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAWallaWallaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAWhitmanCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WA/WAYakimaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIBrownCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIBrownCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIBrownCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WICalumetCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WICalumetCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WICalumetCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WICalumetCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIDaneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIDoorCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIDouglasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIEauClaireParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIFondDuLacCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIKenoshaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIOutagamieCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIOutagamieCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIOutagamieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIOzaukeeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIPeppinCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIRacineCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIRacineCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIRacineCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIRockCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WISheboyganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWalworthCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyAParserTable.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWaukeshaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWinnebagoCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWinnebagoCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WI/WIWinnebagoCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVBerkeleyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVBooneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVCabellCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVFayetteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVGrantCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHampshireCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHardyCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHardyCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHardyCountyCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHardyCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHarrisonCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHarrisonCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVHarrisonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVJeffersonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVKanawhaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVLincolnCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMarionCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMasonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMineralCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMineralCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMineralCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMorganCountyAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMorganCountyBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVMorganCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVPocahontasCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVPrestonCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVRaleighCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVRichieCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVRoaneCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVWoodCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WV/WVWyomingCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WY/WYNatronaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WY/WYParkCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/WY/WYSubletteCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUCadpage2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUCadpage3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUGeneralDashParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUGeneralParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUGeneralSlashParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUMountBarkerParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesDParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZAU/ZAUNewSouthWalesParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABCalgaryParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABCanmoreParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABClearwaterCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABLacombeCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABLamontCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABRedDeerCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAAB/ZCAABStrathconaCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCABC/ZCABCMidIslandRegionParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCABC/ZCABCNanaimoParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCABC/ZCABCPrinceGeorgeParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCANS/ZCANSAnnapolisCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCANS/ZCANSHantsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCANS/ZCANSKingsCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONChathamKentParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAON/ZCAONMississaugaParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZCAQC/ZCAQCQuebecParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZNZ/ZNZAucklandParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZNZ/ZNZCadpage2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZNZ/ZNZCadpage3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZNZ/ZNZNewZealandParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZSE/ZSESwedenParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZUK/ZUKCadpage2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZUK/ZUKCadpage3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZUK/ZUKGeneralDashParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZUK/ZUKGeneralParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZUK/ZUKGeneralSlashParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZUK/ZUKShropshireCountyParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/ZUK/ZUKWestMidlandsParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA10Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA11Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA12Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA13Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA14Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA15Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA16Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA17Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA18Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA19Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA1Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA20Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA21Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA22Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA24Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA25Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA26Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA27Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA29Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA30Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA31Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA32Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA33Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA34Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA35Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA36Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA37Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA38Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA39Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA3AParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA40Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA41Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA43Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA44Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA45Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA46Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA47Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA48Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA49Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA4Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA50Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA51Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA52Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA53Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA54Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA55Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA56Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA57Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA58Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA59Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA5Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA60Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA61Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA62Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA63Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA64Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA65Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA66Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA6Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA7BaseParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA7Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA8Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchA9Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchArchonixParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchB3Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBCParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchBParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchChiefPagingParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchCiscoParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchDAPROParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchEmergitechParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchGeoconxParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchGlobalDispatchParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH01Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchH02Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchInfoSysParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchOSSIParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchPremierOneParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchPrintrakParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchProQAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchProphoenixParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchRedAlert2Parser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchRedAlertParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSPKParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchSouthernPlusParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/dispatch/DispatchVisionAirParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/GeneralAlertParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/GeneralDashParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/GeneralParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/GeneralSlashParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/StandardAParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/StandardNationalWeatherServiceParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/StandardSpottedDogParser.java
A	cadpage-parsers/src/main/java/net/anei/cadpage/parsers/general/XXAcadianAmbulanceParser.java
A	gradle.properties
A	gradle/wrapper/gradle-wrapper.jar
A	gradle/wrapper/gradle-wrapper.properties
A	gradlew
A	gradlew.bat
A	import-summary.txt
A	settings.gradle

[33mcommit 0bef2e718c6eeda59b4aec33e4750e59e96e0197[m
Author: Kenneth Corbin <ken@cadpage.org>
Date:   Wed Sep 21 13:17:27 2016 -0700

    Initial commit

A	LICENSE
A	README.md
