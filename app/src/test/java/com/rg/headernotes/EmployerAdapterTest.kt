package com.rg.headernotes

import com.google.common.truth.Truth
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.ui.employers.EmployerAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class EmployerAdapterTest {
    private var adapter: EmployerAdapter? = null

    @Before
    fun init() {
        adapter = EmployerAdapter(null)
    }

    @Test
    fun addEmployers() {
        Truth.assertThat(adapter).isNotNull()
        adapter?.apply {
            setEmployers(
                listOf(
                    EmployerModel(fullName = "Юдин Антон"),
                    EmployerModel(fullName = "Благодатский Иван"),
                    EmployerModel(fullName = "Бородулина Алина")
                )
            )
            Truth.assertThat(itemCount).isEqualTo(3)
            Truth.assertThat(getEmployer(2).fullName).isEqualTo("Бородулина Алина")
        }
    }

    @Test
    fun deleteEmployer() {
        Truth.assertThat(adapter).isNotNull()
        adapter?.apply {
            setEmployers(
                listOf(
                    EmployerModel(fullName = "Юдин Антон"),
                    EmployerModel(fullName = "Благодатский Иван"),
                    EmployerModel(fullName = "Бородулина Алина")
                )
            )
            Truth.assertThat(itemCount).isEqualTo(3)
            deleteEmployer(2)
            Truth.assertThat(itemCount).isEqualTo(2)
        }
    }

    @Test
    fun addEmployer() {
        Truth.assertThat(adapter).isNotNull()
        adapter?.apply {
            setEmployers(
                listOf(
                    EmployerModel(fullName = "Юдин Антон"),
                    EmployerModel(fullName = "Благодатский Иван"),
                    EmployerModel(fullName = "Бородулина Алина")
                )
            )
            Truth.assertThat(itemCount).isEqualTo(3)
            addEmployer(
                listOf(
                    EmployerModel(fullName = "Александров Игорь")
                )
            )
            Truth.assertThat(itemCount).isEqualTo(4)
            Truth.assertThat(getEmployer(3).fullName)
                .isEqualTo(EmployerModel(fullName = "Александров Игорь").fullName)
        }
    }
}