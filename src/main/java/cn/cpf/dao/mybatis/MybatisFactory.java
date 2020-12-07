package cn.cpf.dao.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2019/11/11 19:22
 **/
public class MybatisFactory {


    private static SqlSessionFactory sqlSessionFactory = null;

    private MybatisFactory(){}

    static {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getInstance(){
        return sqlSessionFactory.openSession();
    }

    public static SqlSession getBatchInstance(){
        return sqlSessionFactory.openSession(ExecutorType.BATCH);
    }

    public <T> int batchUpdate(List<T> tmpQqUserMatches, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method insert = clazz.getDeclaredMethod("insert");
        try (final SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            T mapper = sqlSession.getMapper(clazz);
            int sum = 0;
            for (T t : tmpQqUserMatches) {
                final Object invoke = insert.invoke(mapper, t);
                sum += (int)invoke;
            }
            sqlSession.commit();
            return sum;
        }
    }


}
