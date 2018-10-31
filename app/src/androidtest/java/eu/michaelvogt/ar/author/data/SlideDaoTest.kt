package eu.michaelvogt.ar.author.data

import android.database.sqlite.SQLiteConstraintException
import androidx.test.runner.AndroidJUnit4
import eu.michaelvogt.ar.author.data.utils.LiveDataTestUtil
import eu.michaelvogt.ar.author.data.utils.TestUtil
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SlideDaoTest : DaoTest() {
    private lateinit var slideDao: SlideDao
    private lateinit var visualDetailDao: VisualDetailDao
    private lateinit var areaDao: AreaDao

    @Before
    fun setUp() {
        slideDao = db!!.slideDao()
        visualDetailDao = db!!.visualDetailDao()
        areaDao = db!!.areaDao()
    }

    @Test
    fun getAllFromEmptyTable() {
        val all = LiveDataTestUtil.getValue(slideDao.getAll())

        Assert.assertThat(all, IsEqual.equalTo(emptyList()))
    }

    @Test
    fun getSize() {
        val areaId = areaDao.insert(TestUtil.area1())
        val detailId = visualDetailDao.insert(TestUtil.visualDetail1(areaId))

        slideDao.insertAll(*TestUtil.slides(detailId))
        val size = LiveDataTestUtil.getValue(slideDao.getSize())

        Assert.assertThat(size, IsEqual.equalTo(TestUtil.locations().size))
    }

    @Test
    fun cantInsertDetailWithoutArea() {
        try {
            slideDao.insert(TestUtil.slide1(0))
            Assert.fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun getDetailsForArea() {
        val areaId = areaDao.insert(TestUtil.area1())
        val detailId = visualDetailDao.insert(TestUtil.visualDetail1(areaId))

        slideDao.insertAll(*TestUtil.slides(detailId))

        val details = slideDao.getSlidesForDetail(detailId)

        Assert.assertThat(details.size, IsEqual.equalTo(2))
        Assert.assertThat(details.get(0).description, IsEqual.equalTo(TestUtil.slide1(0).description))
    }
}
