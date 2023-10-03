package com.rg.headernotes

import com.rg.headernotes.models.NoteModel
import com.google.common.truth.Truth.assertThat
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.ui.notes.NoteAdapter
import com.rg.headertasks.ui.tasks.TaskAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class NoteAdapterTest {
    private var adapter: NoteAdapter? = null

    @Before
    fun init() {
        adapter = NoteAdapter(null)
    }

    @Test
    fun addNotes() {
        assertThat(adapter).isNotNull()
        adapter?.apply {
            setNotes(
                listOf(
                    NoteModel("1"),
                    NoteModel("2"),
                    NoteModel("3")
                )
            )
            assertThat(itemCount).isEqualTo(3)
        }
    }

    @Test
    fun deleteNote(){
        assertThat(adapter).isNotNull()
        adapter?.apply {
            setNotes(
                listOf(
                    NoteModel("1"),
                    NoteModel("2"),
                    NoteModel("3")
                )
            )
            assertThat(itemCount).isEqualTo(3)
            deleteNote(2)
            assertThat(itemCount).isEqualTo(2)
        }
    }

    @Test
    fun addNote(){
        assertThat(adapter).isNotNull()
        adapter?.apply {
            setNotes(
                listOf(
                    NoteModel("1"),
                    NoteModel("2"),
                    NoteModel("3")
                )
            )
            assertThat(itemCount).isEqualTo(3)
            addNote(
                listOf(
                    NoteModel("4")
                )
            )
            assertThat(itemCount).isEqualTo(4)
            assertThat(getNote(3)).isEqualTo(NoteModel("4"))
        }
    }
}