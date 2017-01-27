package com.wen;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class GreenDAOGenerator {

    private static final String INDEX = "E:/AndroidStudioProjects/Bangumi/app/src/main/java";

    public static void main(String[] args) throws Exception{

        Schema mSchema = new Schema(1000, "com.wen.bangumi.greenDAO");

        Entity entity = mSchema.addEntity("BangumiItem");
        entity.addIdProperty();
        entity.addIntProperty("bangumi_id").notNull();
        entity.addStringProperty("name_cn");
        entity.addIntProperty("air_weekday");
        entity.addStringProperty("large_image");

        new DaoGenerator().generateAll(mSchema, INDEX);
    }

}
