
<script>
function  a()
{
	p_subj = f.aa.value;
	p_module = f.p_module.value;
	var url ="/servlet/controller.lcms.EduStart?p_process=main&p_subj="+p_subj+"&p_module="+p_module+"&p_year=2010&p_subjseq=0001&p_module=01&p_seq_curm=111";
	window.open(url,"","scrollbars=yes,resizable=yes,width=1024,height=768").focus();
}
</script>


<a href="javascript:a()">START</a>

<form name="f">
subj : 
		<SELECT NAME="aa">
			<option value="164065">164065</option>
			<option value="164062">164062</option>
			<option value="111140">111140</option>
			<option value="159396">159396</option>					
		</SELECT>
		<input type="text" name="p_module"/>	
</form>