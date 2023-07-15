
package com.example.schedulemydiet.models.food;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class TotalNutrients implements Serializable {

    NutrientDetail enercKcal;
    NutrientDetail fat;
    NutrientDetail fasat;
    NutrientDetail chocdf;
    NutrientDetail fibtg;
    NutrientDetail procnt;
    NutrientDetail chole;
    NutrientDetail na;
    NutrientDetail ca;
    NutrientDetail mg;
    NutrientDetail k;
    NutrientDetail fe;
    NutrientDetail zn;
    NutrientDetail p;
    NutrientDetail vitaRae;
    NutrientDetail vitc;
    NutrientDetail thia;
    NutrientDetail ribf;
    NutrientDetail nia;
    NutrientDetail vitb6a;
    NutrientDetail foldfe;
    NutrientDetail vitb12;
    NutrientDetail vitd;
    NutrientDetail tocpha;
    NutrientDetail vitk1;


    //Extra in total
    NutrientDetail fatrn;
    NutrientDetail fams;
    NutrientDetail fapu;
    NutrientDetail cHOCDFNet;
    NutrientDetail sugar;
    NutrientDetail sUGARAdded;
    NutrientDetail folfd;
    NutrientDetail folac;
    NutrientDetail water;

    public List<NutrientDetail> getTotalNutrients() {

        NutrientDetail[] all = {
                enercKcal,fat,fasat,fatrn,chocdf,fibtg,
                procnt,chole,na,ca,mg,k,fe,zn,p,vitaRae,vitc,thia,ribf,
                nia,vitb6a,foldfe,vitb12,vitd,tocpha,vitk1
        };

        return Arrays.asList(all);
    }

    public List<NutrientDetail> getTotalDaily() {

        NutrientDetail[] all = {
                enercKcal,fat,fasat,fams,fapu,chocdf,cHOCDFNet,fibtg,sugar,
                sUGARAdded,procnt,chole,na,ca,mg,k,fe,zn,p,vitaRae,vitc,thia,ribf,
                nia,vitb6a,foldfe,folfd,folac,vitb12,vitd,tocpha,vitk1,water
        };

        return Arrays.asList(all);
    }


}
