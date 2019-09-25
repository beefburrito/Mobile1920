/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "When you are faced with an unfamiliar problem, what do you usually do? ",
                    answers = listOf("Address the problem immediately", "Sit back and let things work out for themselves")),
            Question(text = "Compared with other students, how quickly do you usually complete your class assignments??",
                    answers = listOf("I am usually finished before everyone else", "I finish right on time")),
            Question(text = "Has anyone ever told you that you talk too much?",
                    answers = listOf("Yes", "No")),
            Question(text = "During normal conversation, how quickly do you speak?",
                    answers = listOf("Faster than most people", "Slower than most people")),
            Question(text = "How often do you finish other people's sentences because they speak too slowly?",
                    answers = listOf("Frequently", "Rarely")),
            Question(text = "How often are you late for appointments?",
                    answers = listOf("Rarely", "Most of the time")),
            Question(text = "How would your classmates and friends rate you?",
                    answers = listOf("Hardworking and serious", "Carefree")),
            Question(text = "How often do you worry about future events?",
                    answers = listOf("Constantly", "Rarely"))
    )



    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 7
    private var points = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    points++
                }
                if (answers[answerIndex] == currentQuestion.answers[1]) {
                    questionIndex++
                }
                if (questionIndex < numQuestions) {
                    currentQuestion = questions[questionIndex]
                    setQuestion()
                    binding.invalidateAll()
                }
                if (questionIndex == numQuestions) {
                    if (points > (numQuestions + 1)*3 / 4) {
                        view.findNavController().navigate(R.id.action_gameFragment_to_gameWonFragment)
                    }
                    if (points < (numQuestions + 1)/4) {
                        view.findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
                    }
                    else {
                        view.findNavController().navigate(R.id.action_gameFragment_to_typeAB)
                    }
                }
            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
