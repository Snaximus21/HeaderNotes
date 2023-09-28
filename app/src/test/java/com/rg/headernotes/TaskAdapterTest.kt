package com.rg.headernotes


import com.rg.headernotes.models.TaskModel
import com.rg.headertasks.ui.tasks.TaskAdapter
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class TaskAdapterTest {
    private var adapter: TaskAdapter? = null

    @Before
    fun init() {
        adapter = TaskAdapter(null)
    }

    @Test
    fun addTasks() {
        assertThat(adapter).isNotNull()
        adapter?.let { taskAdapter ->
            taskAdapter.setTasks(
                listOf(
                    TaskModel("1"),
                    TaskModel("2"),
                    TaskModel("3")
                )
            )
            assertThat(taskAdapter.itemCount).isEqualTo(3)
        }
    }

    @Test
    fun deleteTask(){
        assertThat(adapter).isNotNull()
        adapter?.let { taskAdapter ->
            taskAdapter.setTasks(
                listOf(
                    TaskModel("1"),
                    TaskModel("2"),
                    TaskModel("3")
                )
            )
            assertThat(taskAdapter.itemCount).isEqualTo(3)
            taskAdapter.deleteTask(2)
            assertThat(taskAdapter.itemCount).isEqualTo(2)
        }
    }

    @Test
    fun addTask(){
        assertThat(adapter).isNotNull()
        adapter?.let { taskAdapter ->
            taskAdapter.setTasks(
                listOf(
                    TaskModel("1"),
                    TaskModel("2"),
                    TaskModel("3")
                )
            )
            assertThat(taskAdapter.itemCount).isEqualTo(3)
            taskAdapter.addTask(
                listOf(
                    TaskModel("4")
                )
            )
            assertThat(taskAdapter.itemCount).isEqualTo(4)
            assertThat(taskAdapter.getTask(3)).isEqualTo(TaskModel("4"))
        }
    }
}