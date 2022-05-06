package com.youxiang.goods.service;

import com.youxiang.goods.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class GoodsHtmlService {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine engine;  // 模板引擎

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);

    /**
     * 创建html页面
     * @param spuId
     */
    public void createHtml(Long spuId){

        PrintWriter writer = null;
        try {
            // 获取页面数据
            Map<String, Object> map = this.goodsService.loadData(spuId);

            //创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象中
            context.setVariables(map);

            // 创建输出流
            File file = new File("C:\\Tomcat9\\nginx-1.8.0\\html\\item\\"+spuId+".html");
            writer = new PrintWriter(file);

            // 执行页面静态化方法
            engine.process("item",context,writer);

        } catch (FileNotFoundException e) {
            LOGGER.error("页面静态化出错：{}，"+e,spuId);
        }finally {
            if (writer != null){
                writer.close();
            }
        }
    }

    public void asynExcute(Long spuId){
        ThreadUtils.execute(() -> createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }

    public void deleteHtml(Long spuId) {
        File file = new File("C:\\Tomcat9\\nginx-1.8.0\\html\\item\\",spuId+".html");
        file.deleteOnExit();
    }
}
